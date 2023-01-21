package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenericResponse;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PasswordResetTokenRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserSecurityService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class PasswordForgotEndpoint {
  @Value("${frontend.port}")
  private String port;

  @Autowired
  private UserService userService;
  @Autowired
  private PasswordResetTokenRepository tokenRepository;
  @Autowired
  private EmailService emailService;
  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private UserSecurityService userSecurityService;

  private String getAppUrl(HttpServletRequest request) {
    return "http://" + request.getServerName() + ":" + this.port + "#" + request.getContextPath();
  }

  @PermitAll
  @PostMapping("/user/resetPassword")
  public GenericResponse resetPassword(
    HttpServletRequest request, @RequestParam("email") String userEmail) {
    ApplicationUser user = userService.findApplicationUserByEmail(userEmail);
    if (user == null) {
      throw new NotFoundException();
    }
    String token = UUID.randomUUID().toString();
    userService.createPasswordResetTokenForUser(user, token);
    SimpleMailMessage mail = emailService.constructResetTokenEmail(getAppUrl(request), token, user);
    mailSender.send(mail);
    return new GenericResponse("reset password email");
  }

  @PermitAll
  @PostMapping("/user/savePassword")
  public GenericResponse savePassword(@RequestBody PasswordDto passwordDto)
    throws ValidationException {

    final String result = userSecurityService.validatePasswordResetToken(passwordDto.getToken());

    if (result != null) {
      return result.equals("invalidToken")
        ? new GenericResponse("Please provide a valid password reset token.", "invalid token")
        : result.equals("expired")
        ? new GenericResponse(
        "Given token is expired. Please provide a new password reset token.",
        "expired token")
        : new GenericResponse("Something went wrong", "undefinedTokenError");
    }

    Optional<ApplicationUser> user =
      userService.getUserByPasswordResetToken(passwordDto.getToken());
    if (user.isPresent()) {
      userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
      return new GenericResponse("reset password succeeded");
    } else {
      return new GenericResponse("Something went wrong", "undefinedTokenError");
    }
  }
}

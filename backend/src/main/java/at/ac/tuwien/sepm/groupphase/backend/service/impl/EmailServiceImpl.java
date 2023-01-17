package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Service
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class EmailServiceImpl implements EmailService {
  @Value("${support.email}")
  private String email;

  @Override
  public SimpleMailMessage constructResetTokenEmail(String contextPath, String token, ApplicationUser user) {
    String url = contextPath + "/user/changePassword?token=" + token;
    String message = "You have requested a password reset link. Here is your link: \r\n"
      + url + "\r\nThe link will expire after 24 hours. \r\n"
      + "In case you didn't request this link you can simply ignore this message. Your password will not be changed.\r\n\r\n"
      + "Kind regards,\r\n The Ticketline team";
    return constructEmail("Ticketline - Reset Password", message, user);
  }

  private SimpleMailMessage constructEmail(String subject, String body, ApplicationUser user) {
    SimpleMailMessage email = new SimpleMailMessage();
    email.setSubject(subject);
    email.setText(body);
    email.setTo(user.getEmail());
    email.setFrom(this.email);
    return email;
  }
}

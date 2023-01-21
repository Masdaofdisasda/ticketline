package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/user/register")
public class RegistrationEndpoint {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final UserService userService;

  public RegistrationEndpoint(UserService userService) {
    this.userService = userService;
  }

  @PermitAll
  @PostMapping
  public void register(@RequestBody UserRegistrationDto userRegistrationDto)
      throws ValidationException {
    LOGGER.info("GET /api/v1/user/ body: {}", userRegistrationDto);
    userService.register(userRegistrationDto);
  }
}

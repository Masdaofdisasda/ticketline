package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class UserDataGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String TEST_ADMIN_EMAIL = "admin@email.com";
  private static final String TEST_ADMIN_PSW = "password";
  private static final String TEST_ADMIN_FIRST_NAME = "Max";
  private static final String TEST_ADMIN_LAST_NAME = "Mustermann";

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  public UserDataGenerator(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @PostConstruct
  private void generateAdminUser() {
    if (userRepository.findAll().size() > 0) {
      LOGGER.debug("user already generated");
    } else {
      LOGGER.debug("generating admin user entry");
      ApplicationUser admin = new ApplicationUser(
        TEST_ADMIN_EMAIL,
        passwordEncoder.encode(TEST_ADMIN_PSW),
        TEST_ADMIN_FIRST_NAME,
        TEST_ADMIN_LAST_NAME,
        true
      );
      LOGGER.debug("saving user {}", admin);
      userRepository.save(admin);
    }
  }

}

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
import java.util.Date;

@Profile("generateData")
@Component
public class UserDataGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String TEST_ADMIN_EMAIL = "admin@email.com";
  private static final String TEST_ADMIN_PSW = "password";
  private static final String TEST_ADMIN_FIRST_NAME = "Max";
  private static final String TEST_ADMIN_LAST_NAME = "Mustermann";

  private static final String TEST_LOCKED_EMAIL = "viktor@email.com";
  private static final String TEST_LOCKED_PSW = "password";
  private static final String TEST_LOCKED_FIRST_NAME = "Viktor";
  private static final String TEST_LOCKED_LAST_NAME = "Vergesslich";

  private static final String TEST_USER_EMAIL = "sepm@test.com";
  private static final String TEST_USER_PSW = "sepmtest";
  private static final String TEST_USER_FIRST_NAME = "Jakob";
  private static final String TEST_USER_LAST_NAME = "Bauer";

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  public UserDataGenerator(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @PostConstruct
  private void generateAll() {
    userRepository.deleteAll();
    generateAdminUser();
    generateLockedUser();
    generateRegularUser();
  }

  private void generateAdminUser() {
    LOGGER.debug("generating admin user entry");
    ApplicationUser admin = ApplicationUser.builder()
      .email(TEST_ADMIN_EMAIL)
      .password(this.passwordEncoder.encode(TEST_ADMIN_PSW))
      .firstName(TEST_ADMIN_FIRST_NAME)
      .lastName(TEST_ADMIN_LAST_NAME)
      .accountNonLocked(true)
      .admin(true)
      .build();
    LOGGER.debug("saving user {}", admin);
    userRepository.save(admin);
  }

  private void generateLockedUser() {
    LOGGER.debug("generating admin user entry");
    ApplicationUser user = ApplicationUser.builder()
      .email(TEST_LOCKED_EMAIL)
      .password(this.passwordEncoder.encode(TEST_LOCKED_PSW))
      .firstName(TEST_LOCKED_FIRST_NAME)
      .lastName(TEST_LOCKED_LAST_NAME)
      .accountNonLocked(false)
      .lockTime(new Date())
      .failedAttempt(5)
      .admin(false)
      .build();
    LOGGER.debug("saving user {}", user);
    userRepository.save(user);
  }

  private void generateRegularUser() {
    LOGGER.debug("generating users");
    ApplicationUser user = ApplicationUser.builder()
      .email(TEST_USER_EMAIL)
      .password(this.passwordEncoder.encode(TEST_USER_PSW))
      .firstName(TEST_USER_FIRST_NAME)
      .lastName(TEST_USER_LAST_NAME)
      .accountNonLocked(true)
      .admin(false)
      .build();

    LOGGER.debug("saving user {}", user);
    userRepository.save(user);
  }
}

package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * CustomUserValidator.
 */
@Component
public class CustomUserValidator {
  private final UserRepository userRepository;
  private static final Logger LOGGER =
    LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public CustomUserValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private List<String> validatePassword(String password) {
    List<String> validationErrors = new ArrayList<>();
    if (password == null || password.isBlank()) {
      validationErrors.add("no password given");
    }
    if (password.length() < 8) {
      validationErrors.add("password is too short");
    }

    return validationErrors;
  }

  private List<String> validateRequiredStringProperty(String str, String propertyName) {
    List<String> validationErrors = new ArrayList<>();
    if (str == null || str.isBlank()) {
      validationErrors.add("no " + propertyName + " given");
    }

    return validationErrors;
  }


  private List<String> validateUserEmail(String email) {
    List<String> validationErrors = new ArrayList<>();
    if (email != null) {
      if (email.isBlank()) {
        validationErrors.add("Email of user is given but blank");
      }
      if (email.length() > 255) {
        validationErrors.add("Email of user too long: longer than 255 characters");
      }
      String regex = "^(.+)@(.+)$";
      // Compile regular expression to get the pattern
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(email);

      if (!matcher.matches()) {
        validationErrors.add("Email address is not valid");
      }
    } else {
      validationErrors.add("Email of user is required but null");
    }

    return validationErrors;
  }

  /**
   * Validates user data for the create operation.
   *
   * @param user user data
   * @throws ValidationException on invalid data
   */
  public void validateForCreate(UserRegistrationDto user) throws ValidationException {
    LOGGER.trace("validateForCreate({})", user);
    List<String> validationErrors = new ArrayList<>();

    ApplicationUser applicationUser = userRepository.findUserByEmail(user.getEmail());
    if (applicationUser != null) {
      validationErrors.add("A user with the given email address already exists.");
    }

    validationErrors.addAll(validateUserEmail(user.getEmail()));
    validationErrors.addAll(validatePassword(user.getPassword()));
    validationErrors.addAll(validateRequiredStringProperty(user.getFirstName(), "first name"));
    validationErrors.addAll(validateRequiredStringProperty(user.getLastName(), "last name"));
    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of user for create failed", validationErrors);
    }
  }
}

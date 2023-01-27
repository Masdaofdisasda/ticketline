package at.ac.tuwien.sepm.groupphase.backend.service.validator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    LOGGER.trace("validatePassword({})", password);

    List<String> validationErrors = new ArrayList<>();
    if (password == null || password.isBlank()) {
      validationErrors.add("No password given.");
    }
    assert password != null;
    if (password.length() < 8) {
      validationErrors.add("Password is too short.");
    }

    return validationErrors;
  }

  private List<String> validateRequiredStringProperty(String str, String propertyName) {
    LOGGER.trace("validateRequiredStringProperty({}, {})", str, propertyName);

    List<String> validationErrors = new ArrayList<>();
    if (str == null || str.isBlank()) {
      validationErrors.add("No " + propertyName + " given.");
    }

    return validationErrors;
  }

  private List<String> validateUserEmail(String email) {
    LOGGER.trace("validateUserEmail({})", email);

    List<String> validationErrors = new ArrayList<>();
    if (email != null) {
      if (email.isBlank()) {
        validationErrors.add("Email of user is given but blank.");
      }
      if (email.length() > 255) {
        validationErrors.add("Email of user too long: longer than 255 characters.");
      }
      String regex = "^(.+)@(.+)$";
      // Compile regular expression to get the pattern
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(email);

      if (!matcher.matches()) {
        validationErrors.add("Email address is not valid.");
      }
    } else {
      validationErrors.add("Email of user is required but null.");
    }

    return validationErrors;
  }

  private List<String> validateIsAdmin(Boolean isAdmin) {
    LOGGER.trace("validateIsAdmin({})", isAdmin);

    List<String> validationErrors = new ArrayList<>();

    if (isAdmin == null) {
      validationErrors.add("IsAdmin of user is required but null.");
    }

    return validationErrors;
  }

  /**
   * Validates user data for the create operation.
   *
   * @param user user data
   * @throws ValidationException on invalid data
   */
  public void validateForRegister(UserRegistrationDto user) throws ValidationException {
    LOGGER.trace("validateForRegister({})", user);

    List<String> validationErrors = new ArrayList<>();

    Optional<ApplicationUser> applicationUser = userRepository.findUserByEmail(user.getEmail());
    if (applicationUser.isPresent()) {
      validationErrors.add("A user with the given email address already exists.");
    }

    validationErrors.addAll(validateUserEmail(user.getEmail()));
    validationErrors.addAll(validatePassword(user.getPassword()));
    validationErrors.addAll(validateRequiredStringProperty(user.getFirstName(), "first name"));
    validationErrors.addAll(validateRequiredStringProperty(user.getLastName(), "last name"));
    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of user for create failed.", validationErrors);
    }
  }

  /**
   * Validates user data for the create operation.
   *
   * @param user user data
   * @throws ValidationException on invalid data
   */
  public void validateForCreate(UserCreationDto user) throws ValidationException {
    LOGGER.trace("validateForCreate({})", user);

    List<String> validationErrors = new ArrayList<>();

    Optional<ApplicationUser> applicationUser = userRepository.findUserByEmail(user.getEmail());
    if (applicationUser.isPresent()) {
      validationErrors.add("A user with the given email address already exists.");
    }

    validationErrors.addAll(validateUserEmail(user.getEmail()));
    validationErrors.addAll(validateIsAdmin(user.getIsAdmin()));
    validationErrors.addAll(validatePassword(user.getPassword()));
    validationErrors.addAll(validateRequiredStringProperty(user.getFirstName(), "first name"));
    validationErrors.addAll(validateRequiredStringProperty(user.getLastName(), "last name"));
    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of user for create failed.", validationErrors);
    }
  }


  public void validateForUpdate(SimpleUserDto userDto) throws ValidationException {
    LOGGER.trace("validateForUpdate({})", userDto);

    List<String> validationErrors = new ArrayList<>();

    final String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    ApplicationUser user = userRepository.findUserByEmail(email)
      .orElseThrow(() -> new NotFoundException("User with email %s not found".formatted(email)));
    if (!Objects.equals(user.getId(), userDto.getId())) {
      validationErrors.add("User can only change his own data.");
    }
    validationErrors.addAll(validateUserEmail(user.getEmail()));
    validationErrors.addAll(validateRequiredStringProperty(user.getFirstName(), "first name"));
    validationErrors.addAll(validateRequiredStringProperty(user.getLastName(), "last name"));
    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of user for create failed.", validationErrors);
    }

  }

  public void validateForDelete(Long userId) throws ValidationException {
    LOGGER.trace("validateForDelete({})", userId);

    var roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

    if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
      final String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

      ApplicationUser user = userRepository.findUserByEmail(email)
        .orElseThrow(() -> new NotFoundException("User with email %s not found".formatted(email)));
      if (!Objects.equals(user.getId(), userId)) {
        throw new ValidationException("Cannot delete other users.", List.of("Cannot delete other users."));
      }

    } else if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
      ApplicationUser user = userRepository.findUserById(userId);
      if (user.isAdmin()) {
        throw new ValidationException("Cannot delete admin users.", List.of("Cannot delete admin users."));
      }
    }
  }

  public void validateNewPassword(String password) throws ValidationException {
    LOGGER.trace("validateNewPassword({})", password);

    List<String> validationErrors = new ArrayList<>(validatePassword(password));
    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of password failed", validationErrors);
    }
  }
}

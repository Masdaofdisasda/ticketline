package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomLockedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserService {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenizer jwtTokenizer;
  private final CustomUserValidator customUserValidator;

  @Autowired
  public CustomUserDetailService(
    UserRepository userRepository,
    PasswordEncoder passwordEncoder,
    JwtTokenizer jwtTokenizer,
    CustomUserValidator customUserValidator) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenizer = jwtTokenizer;
    this.customUserValidator = customUserValidator;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    LOGGER.debug("Load all user by email");
    try {
      ApplicationUser applicationUser = findApplicationUserByEmail(email);

      List<GrantedAuthority> grantedAuthorities;
      if (Boolean.TRUE.equals(applicationUser.getAdmin())) {
        grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
      } else {
        grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
      }
      return new User(
        applicationUser.getEmail(),
        applicationUser.getPassword(),
        true,
        true,
        true,
        applicationUser.isAccountNonLocked(),
        grantedAuthorities);
    } catch (NotFoundException e) {
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }

  @Override
  public ApplicationUser findApplicationUserByEmail(String email) {
    LOGGER.debug("Find application user by email");
    ApplicationUser applicationUser = userRepository.findUserByEmail(email);
    if (applicationUser != null) {
      return applicationUser;
    }
    throw new NotFoundException(
      String.format("Could not find the user with the email address %s", email));
  }

  public ApplicationUser findApplicationUserById(Long id) {
    LOGGER.debug("Find application user by Id");
    ApplicationUser applicationUser = userRepository.findUserById(id);
    if (applicationUser != null) {
      return applicationUser;
    }
    throw new NotFoundException(
      String.format("Could not find the user with the id %d", id));
  }

  private void handleAuthenticationError(String email) {
    ApplicationUser user = this.findApplicationUserByEmail(email);
    if (user != null) {
      if (user.isAccountNonLocked()) {
        if (user.getFailedAttempt() < MAX_FAILED_ATTEMPTS - 1) {
          this.increaseFailedAttempts(user);
        } else {
          this.lock(user);
          throw new CustomLockedException("Your account has been locked due to 5 failed attempts."
            + " It will be unlocked after 24 hours.");
        }
      }
    }
  }

  private void handleAuthenticationSuccess(String email) {
    ApplicationUser user = this.findApplicationUserByEmail(email);
    if (user.getFailedAttempt() > 0) {
      this.resetFailedAttempts(user.getEmail());
    }
  }

  @Override
  public String login(UserLoginDto userLoginDto) {
    UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
    if (userDetails != null
      && userDetails.isAccountNonExpired()
      && userDetails.isCredentialsNonExpired()) {
      if (!userDetails.isAccountNonLocked()) {
        ApplicationUser user = this.findApplicationUserByEmail(userLoginDto.getEmail());
        if (this.unlockWhenTimeExpired(user)) {
          throw new LockedException("Your account has been unlocked. Please try to login again.");
        } else {
          throw new CustomLockedException("Your account is locked.");
        }
      } else if (passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())) {
        List<String> roles =
          userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        handleAuthenticationSuccess(userLoginDto.getEmail());
        return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
      }
      handleAuthenticationError(userLoginDto.getEmail());
    }
    throw new BadCredentialsException("Username or password is incorrect.");
  }

  @Override
  public void register(UserRegistrationDto userRegistrationDto) throws ValidationException {
    customUserValidator.validateForCreate(userRegistrationDto);
    ApplicationUser applicationUser =
      ApplicationUser.builder()
        .email(userRegistrationDto.getEmail())
        .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
        .firstName(userRegistrationDto.getFirstName())
        .lastName(userRegistrationDto.getLastName())
        .admin(false)
        .accountNonLocked(true)
        .build();
    userRepository.save(applicationUser);
  }

  public void increaseFailedAttempts(ApplicationUser user) {
    int newFailAttempts = user.getFailedAttempt() + 1;
    userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
  }

  public void resetFailedAttempts(String email) {
    userRepository.updateFailedAttempts(0, email);
  }

  @Override
  public List<ApplicationUser> getLockedUsers() {
    return userRepository.getLockedUsers();
  }

  @Override
  public List<ApplicationUser> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public SimpleUserDto getUser() {
    ApplicationUser user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    return SimpleUserDto.builder()
      .id(user.getId())
      .email(user.getEmail())
      .firstName(user.getFirstName())
      .lastName(user.getLastName())
      .build();
  }

  @Override
  public SimpleUserDto updateUser(SimpleUserDto userData) throws ValidationException {
    customUserValidator.validateForUpdate(userData);

    ApplicationUser user = userRepository.findUserById(userData.getId());
    user.setEmail(userData.getEmail());
    user.setFirstName(userData.getFirstName());
    user.setLastName(userData.getLastName());

    ApplicationUser updatedUser = userRepository.save(user);

    return SimpleUserDto.builder()
      .id(updatedUser.getId())
      .firstName(updatedUser.getFirstName())
      .lastName(updatedUser.getLastName())
      .email(updatedUser.getEmail())
      .build();
  }

  @Override
  public void deleteUser(Long userId) throws ValidationException {

    customUserValidator.validateForDelete(userId);
    userRepository.deleteById(userId);
  }

  public void lock(ApplicationUser user) {
    user.setAccountNonLocked(false);
    user.setLockTime(new Date());

    userRepository.save(user);
  }

  public void unlock(ApplicationUser user) {
    user.setAccountNonLocked(true);
    user.setLockTime(null);
    user.setFailedAttempt(0);

    userRepository.save(user);
  }

  public boolean unlockWhenTimeExpired(ApplicationUser user) {
    long lockTimeInMillis = user.getLockTime().getTime();
    long currentTimeInMillis = System.currentTimeMillis();

    if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
      this.unlock(user);
      return true;
    }

    return false;
  }
}

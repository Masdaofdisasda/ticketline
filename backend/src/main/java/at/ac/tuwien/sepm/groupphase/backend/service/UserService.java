package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public interface UserService extends UserDetailsService {

  static final long MAX_FAILED_ATTEMPTS = 5;

  /**
   * Find a user in the context of Spring Security based on the email address <br>
   * For more information have a look at this tutorial:
   * https://www.baeldung.com/spring-security-authentication-with-a-database
   *
   * @param email the email address
   * @return a Spring Security user
   * @throws UsernameNotFoundException is thrown if the specified user does not exists
   */
  @Override
  UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

  /**
   * Find an application user based on the email address.
   *
   * @param email the email address
   * @return an application user
   */
  ApplicationUser findApplicationUserByEmail(String email);

  /**
   * Find an application user based on the ID.
   *
   * @param id the id of the user
   * @return an application user
   */
  ApplicationUser findApplicationUserById(Long id);

  /**
   * Log in a user.
   *
   * @param userLoginDto login credentials
   * @return the JWT, if successful
   * @throws org.springframework.security.authentication.BadCredentialsException if credentials are
   *                                                                             bad
   */
  String login(UserLoginDto userLoginDto) throws BadCredentialsException;

  /**
   * Register a new user.
   *
   * @param userRegistrationDto user registration information (including credentials)
   * @throws ValidationException when validation for userRegistrationDto fails
   */
  void register(UserRegistrationDto userRegistrationDto) throws ValidationException;

  /**
   * Increase failed attempts to authenticate by 1.
   *
   * @param user the user for which the login attempts should be increased.
   */
  void increaseFailedAttempts(ApplicationUser user);

  /**
   * Unlocks the user if the time after locking the account has expired.
   *
   * @param user The user which will be unlocked in case the expiration time has passed.
   * @return true when the user was unlocked, false otherwise
   */
  boolean unlockWhenTimeExpired(ApplicationUser user);

  /**
   * Locks the given user account, therefore prevents the user from logging in.
   *
   * @param user The user account that will be locked.
   */
  void lock(ApplicationUser user);

  /**
   * Unlocks the given user account, therefore allows the user to log in.
   *
   * @param user The user account that will be unlocked.
   */
  void unlock(ApplicationUser user);

  /**
   * Reset the failed login attempts to 0 for user with the given email.
   *
   * @param email of the user for which the login attempts will be set to 0.
   */
  void resetFailedAttempts(String email);

  /**
   * Fetch all currently locked users.
   *
   * @return List of locked users.
   */
  List<ApplicationUser> getLockedUsers();
}

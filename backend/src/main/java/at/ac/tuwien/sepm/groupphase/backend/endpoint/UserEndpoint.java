package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserLockedDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomLockedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserEndpoint {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final UserService userService;
  private final UserMapper userMapper;

  public UserEndpoint(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("locked")
  public List<SimpleUserDto> getLocked() {
    LOGGER.info("GET /api/v1/user/locked");
    return userMapper.userToSimpleUserDto(userService.getLockedUsers());
  }

  @Secured("ROLE_ADMIN")
  @GetMapping(value = "/all")
  public List<SimpleUserDto> getAll() {
    LOGGER.info("GET /api/v1/user");
    return userMapper.userToSimpleUserDto(userService.getUsers());
  }

  @Secured("ROLE_ADMIN")
  @PutMapping("/{id}/accountNonLocked")
  public void updateLocked(@PathVariable Long id, @RequestBody UpdateUserLockedDto updateUserLockedDto) {
    LOGGER.info("PUT /api/v1/user/{}/accountNonLocked body: {}", updateUserLockedDto);
    ApplicationUser user = userService.findApplicationUserById(id);
    if (updateUserLockedDto.isAccountNonLocked()) {
      userService.unlock(user);
    } else {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String currentPrincipalName = authentication.getName();
      ApplicationUser currentUser = userService.findApplicationUserByEmail(currentPrincipalName);
      if (!Objects.equals(currentUser.getId(), id)) {
        userService.lock(user);
      } else {
        throw new CustomLockedException("A user can't lock their own account");
      }
    }
  }

  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping
  public SimpleUserDto getCurrentUser() {
    LOGGER.info("GET /api/v1/user");
    return userService.getUser();
  }

  @Secured("ROLE_USER")
  @PutMapping(value = "/{userId}")
  public SimpleUserDto updateUserData(@RequestBody SimpleUserDto userData, @PathVariable Long userId) throws ValidationException {
    LOGGER.info("GET /api/v1/user/{} {}", userId, userData);
    userData.setId(userId);
    return userService.updateUser(userData);
  }

  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @DeleteMapping(value = "{userId}")
  public void deleteUser(@PathVariable Long userId) throws ValidationException {
    LOGGER.info("GET /api/v1/user/{}", userId);
    userService.deleteUser(userId);
  }

  @Secured("ROLE_ADMIN")
  @PostMapping
  public void createUser(@RequestBody UserCreationDto userCreationDto)
    throws ValidationException {
    LOGGER.info("GET /api/v1/user/ body: {}", userCreationDto);
    userService.createUser(userCreationDto);
  }

}

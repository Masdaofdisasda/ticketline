package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserEndpointTest implements TestData {
  /*
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserMapper userMapper;

  private UserLoginDto adminDto = UserLoginDto.builder()
    .email("admin@email.com")
    .password("12345678")
    .build();

  @BeforeEach
  public void beforeEach() {
    userRepository.deleteAll();
    ApplicationUser adminUser =
      ApplicationUser.builder()
        .email("admin@email.com")
        .firstName("Max")
        .lastName("Mustermann")
        .admin(true)
        .accountNonLocked(true)
        .password(
          passwordEncoder.encode("12345678")
        ).build();
    userRepository.save(adminUser);
    ApplicationUser lockedApplicationUser =
      ApplicationUser.builder()
        .email("viktor@email.com")
        .firstName("Viktor")
        .lastName("Vergesslich")
        .admin(false)
        .accountNonLocked(false)
        .lockTime(new Date())
        .password(
          passwordEncoder.encode("12345678")
        ).build();
    userRepository.save(lockedApplicationUser);
  }

  //getLocked
  //updateLocked
  @Test
  public void getLockedUsersWithAdmin() throws Exception {
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          get(USER_BASE_URI + "/locked")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());

    List<SimpleUserDto> simpleUserDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
      SimpleUserDto[].class));

    assertEquals(1, simpleUserDtos.size());
  }

  @Test
  public void getLockedUsersFailsWithoutAdminRoles() throws Exception {
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          get(USER_BASE_URI + "/locked")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

  @Test
  public void getUsersWithAdmin() throws Exception {
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          get(USER_BASE_URI + "/all")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());

    List<SimpleUserDto> simpleUserDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
      SimpleUserDto[].class));

    assertEquals(2, simpleUserDtos.size());
  }

  @Test
  public void getUsersFailsWithoutAdminRoles() throws Exception {
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          get(USER_BASE_URI + "/all")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

  @Test
  public void unlockUserWithAdmin() throws Exception {
    ApplicationUser user = userRepository.findUserByEmail("viktor@email.com");
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          put(USER_BASE_URI + "/{id}/accountNonLocked", user.getId())
            .content(objectMapper.writeValueAsString(new UpdateUserLockedDto(true)))
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());

    List<ApplicationUser> lockedUsers = userRepository.getLockedUsers();

    assertEquals(0, lockedUsers.size());
  }

  @Test
  public void unlockUserFailsWithoutAdminAccess() throws Exception {
    ApplicationUser user = userRepository.findUserByEmail("viktor@email.com");
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          put(USER_BASE_URI + "/{id}/accountNonLocked", user.getId())
            .content(objectMapper.writeValueAsString(new UpdateUserLockedDto(true)))
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

  @Test
  void AdminCannotUpdateUserData() throws Exception {
    ApplicationUser user = userRepository.findUserByEmail("viktor@email.com");

    MvcResult mvcResult =
      this.mockMvc
        .perform(
          put(USER_BASE_URI + "/{id}", user.getId())
            .content(objectMapper.writeValueAsString(new SimpleUserDto(user.getEmail(), "Firstname", "Lastname", user.getId(), false)))
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }


  @Test
  public void createAdminUserWithUserCreateEndpoint() throws Exception {
    UserCreationDto userDto = UserCreationDto.builder()
      .email("test@tst.com")
      .password("12345678")
      .firstName("Max")
      .lastName("Mustermann")
      .isAdmin(true)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    ApplicationUser createdUser = userRepository.findUserByEmail(userDto.getEmail());
    assertEquals(createdUser.getEmail(), userMapper.userCreationDtoToApplicationUser(userDto).getEmail());
  }

  @Test
  public void failCreateUserWhenUserExistsAlready() throws Exception {
    ApplicationUser user = ApplicationUser.builder()
      .email("test@test.com")
      .firstName("Max")
      .lastName("Mustermann")
      .admin(false)
      .password(
        passwordEncoder.encode("12345678")
      ).build();

    UserCreationDto userDto = UserCreationDto.builder()
      .email("test@test.com")
      .password("12345678")
      .firstName("Max")
      .lastName("Mustermann")
      .isAdmin(true)
      .build();

    userRepository.save(user);
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failCreateUserWhenEmailIsNotGiven() throws Exception {
    UserCreationDto userDto = UserCreationDto.builder()
      .password("12345678")
      .firstName("Max")
      .lastName("Mustermann")
      .isAdmin(true)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failCreateUserWhenFirstNameIsNotGiven() throws Exception {
    UserCreationDto userDto = UserCreationDto.builder()
      .email("test@test.com")
      .password("12345678")
      .lastName("Mustermann")
      .isAdmin(true)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failCreateUserWhenLastNameIsNotGiven() throws Exception {
    UserCreationDto userDto = UserCreationDto.builder()
      .email("test@test.com")
      .password("12345678")
      .firstName("Max")
      .isAdmin(true)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failCreateUserWhenEmailIsMalformed() throws Exception {
    UserCreationDto userDto = UserCreationDto.builder()
      .email("test")
      .password("12345678")
      .firstName("Max")
      .lastName("Mustermann")
      .isAdmin(true)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failCreateUserWhenIsAdminIsNotGiven() throws Exception {
    UserCreationDto userDto = UserCreationDto.builder()
      .email("test@test.com")
      .password("12345678")
      .firstName("Max")
      .lastName("Mustermann")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failCreateUserWhenUserIsNotAdmin() throws Exception {
    UserCreationDto userDto = UserCreationDto.builder()
      .email("test@tst.com")
      .password("12345678")
      .firstName("Max")
      .lastName("Mustermann")
      .isAdmin(true)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

   */
}

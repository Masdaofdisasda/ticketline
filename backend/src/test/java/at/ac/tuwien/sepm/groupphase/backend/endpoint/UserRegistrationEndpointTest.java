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
public class UserRegistrationEndpointTest implements TestData {
/*
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private NewsMapper newsMapper;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private ApplicationUser user;

  private UserRegistrationDto userDto = UserRegistrationDto.builder()
    .email("test@test.com")
    .password("12345678")
    .firstName("Max")
    .lastName("Mustermann")
    .build();

  @BeforeEach
  public void beforeEach() {
    userRepository.deleteAll();
    user = ApplicationUser.builder()
      .email("test@test.com")
      .firstName("Max")
      .lastName("Mustermann")
      .admin(false)
      .password(
        passwordEncoder.encode("12345678")
      ).build();
  }

  @Test
  public void createUserWithRegistrationEndpoint() throws Exception {
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    ApplicationUser createdUser = userRepository.findUserByEmail(userDto.getEmail());
    assertEquals(createdUser.getEmail(), userMapper.userRegistrationDtoToApplicationUser(userDto).getEmail());
  }

  @Test
  public void failWhenUserExistsAlready() throws Exception {
    userRepository.save(user);
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failWhenEmailIsNotGiven() throws Exception {
    UserRegistrationDto userDto = UserRegistrationDto.builder()
      .email(null)
      .password("12345678")
      .firstName("Max")
      .lastName("Mustermann")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failWhenFirstNameIsNotGiven() throws Exception {
    UserRegistrationDto userDto = UserRegistrationDto.builder()
      .email("test@test.com")
      .password("12345678")
      .lastName("Mustermann")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failWhenLastNameIsNotGiven() throws Exception {
    UserRegistrationDto userDto = UserRegistrationDto.builder()
      .email("test@test.com")
      .password("12345678")
      .lastName("Mustermann")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void failWhenEmailIsMalformed() throws Exception {
    UserRegistrationDto userDto = UserRegistrationDto.builder()
      .email("email")
      .password("12345678")
      .firstName("Max")
      .lastName("Mustermann")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

 */
}

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
public class UserLoginEndpointTest implements TestData {
/*
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper objectMapper;

  private UserLoginDto userDto = UserLoginDto.builder()
    .email("test@test.com")
    .password("12345678")
    .build();

  @BeforeEach
  public void beforeEach() {
    userRepository.deleteAll();
    ApplicationUser applicationUser =
      ApplicationUser.builder()
        .email("test@test.com")
        .firstName("Max")
        .lastName("Mustermann")
        .admin(false)
        .accountNonLocked(true)
        .password(
          passwordEncoder.encode("12345678")
        ).build();
    userRepository.save(applicationUser);
  }

  @Test
  public void loginUserWithLoginEndpoint() throws Exception {
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(AUTH_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNotNull(response.getContentAsString());
  }

  @Test
  public void failWhenEmailIsNull() throws Exception {
    UserLoginDto userDto = UserLoginDto.builder()
      .email(null)
      .password("12345678")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(AUTH_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void failWhenPasswordIsNotGiven() throws Exception {
    UserLoginDto userDto = UserLoginDto.builder()
      .email("test@test.com")
      .password("")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(AUTH_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

  @Test
  public void failWhenCredentialsAreWrong() throws Exception {
    UserLoginDto userDto = UserLoginDto.builder()
      .email("test@test.com")
      .password("123456789")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(AUTH_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

  @Test
  public void failWhenUserIsLocked() throws Exception {
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
    UserLoginDto userDto = UserLoginDto.builder()
      .email("viktor@email.com")
      .password("12345678")
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(AUTH_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
  }

 */
}

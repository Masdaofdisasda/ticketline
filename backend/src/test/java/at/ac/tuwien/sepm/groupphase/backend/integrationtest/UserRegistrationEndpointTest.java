package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.MessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserRegistrationEndpointTest implements TestData {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MessageMapper messageMapper;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private SecurityProperties securityProperties;

  private ApplicationUser user = new ApplicationUser("test@test.com", "12345678", "Max", "Mustermann", false);

  private UserRegistrationDto userDto = UserRegistrationDto.builder()
    .email("test@test.com")
    .password("12345678")
    .firstName("Max")
    .lastName("Mustermann")
    .build();

  @BeforeEach
  public void beforeEach() {
    userRepository.deleteAll();
  }

  @Test
  public void createUserWithRegistrationEndpoint() throws Exception {
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI)
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
          post(USER_BASE_URI)
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
          post(USER_BASE_URI)
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
          post(USER_BASE_URI)
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
          post(USER_BASE_URI)
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
          post(USER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }
}

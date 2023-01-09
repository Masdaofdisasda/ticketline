package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenericResponse;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.PasswordResetTokenRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.PasswordResetToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PasswordForgotEndpointTest implements TestData {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper objectMapper;

  private ApplicationUser user;

  @Mock
  JavaMailSender javaMailSender;

  @InjectMocks
  @Autowired
  PasswordForgotEndpoint passwordForgotEndpoint;

  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.initMocks(this);
    passwordResetTokenRepository.deleteAll();
    userRepository.deleteAll();
    user = ApplicationUser.builder()
      .email("test@test.com")
      .firstName("Max")
      .lastName("Mustermann")
      .admin(false)
      .password(
        passwordEncoder.encode("12345678")
      ).build();
    userRepository.save(user);
  }

  // Tried to mock javaMailSender and call resetPassword manually with mocked HttpServletRequest without success
  // Tried to inject mocked javaMailSender with @MockBean without success
//  @Test
//  public void resetPasswordSendsMailWhenEmailIsFound() {
//    MockHttpServletRequest request = new MockHttpServletRequest();
//
//    passwordForgotEndpoint.resetPassword(request, "test@test.com");
//
//    ArgumentCaptor<SimpleMailMessage> emailCaptor =
//      ArgumentCaptor.forClass(SimpleMailMessage.class);
//    verify(javaMailSender, times(1)).send(emailCaptor.capture());
//
//    List<SimpleMailMessage> actualList = emailCaptor.getAllValues();
//    assertEquals(actualList.size(), 1);
//    assertEquals(actualList.get(0).getSubject(), "Reset Password");
//  }

  @Test
  public void resetPasswordRequestReturnsOKWhenEmailIsFound() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("email", "test@test.com");

    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/resetPassword")
            .contentType(MediaType.APPLICATION_JSON)
            .params(requestParams))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNotNull(response.getContentAsString());
  }

  @Test
  public void requestReturns404WhenEmailIsNotFound() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("email", "tes@test.com");

    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/resetPassword")
            .contentType(MediaType.APPLICATION_JSON)
            .params(requestParams))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    assertNotNull(response.getContentAsString());
  }

  @Test
  public void savePasswordSucceedsWhenTokenIsFound() throws Exception {
    String token = "31fc366d-7c1c-41dc-9384-09f2537e7c48";
    passwordResetTokenRepository.save(new PasswordResetToken(token, user));
    PasswordDto passwordDto = PasswordDto.builder()
      .newPassword("password")
      .token(token)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/savePassword")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNotNull(response.getContentAsString());
    ApplicationUser user1 = userRepository.findUserByEmail("test@test.com");
    assertTrue(passwordEncoder.matches("password", user1.getPassword()));
  }

  @Test
  public void savePasswordFailsWhenTokenIsNotFound() throws Exception {
    String token = "23fc366d-7c1c-41dc-9386-09f2537e5c48";
    PasswordDto passwordDto = PasswordDto.builder()
      .newPassword("password")
      .token(token)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/savePassword")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNotNull(response.getContentAsString());
    String responseString = response.getContentAsString();
    GenericResponse genericResponse = objectMapper.readValue(responseString,
      GenericResponse.class);
    assertEquals("invalid token", genericResponse.getError());
    assertNotNull(response.getContentAsString());

    ApplicationUser user1 = userRepository.findUserByEmail("test@test.com");
    assertFalse(passwordEncoder.matches("password", user1.getPassword()));
  }

  @Test
  public void savePasswordFailsWhenTokenIsExpired() throws Exception {
    String token = "23fc366d-7c1c-41dc-9386-09f2537e5c48";
    PasswordResetToken prt = PasswordResetToken.builder()
      .token("23fc366d-7c1c-41dc-9386-09f2537e5c48")
      .user(user)
      .build();
    Date date = new Date(0);
    prt.setExpiryDate(date);

    passwordResetTokenRepository.save(prt);
    PasswordDto passwordDto = PasswordDto.builder()
      .newPassword("password")
      .token(token)
      .build();
    MvcResult mvcResult =
      this.mockMvc
        .perform(
          post(USER_BASE_URI + "/savePassword")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordDto)))
        .andDo(print())
        .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNotNull(response.getContentAsString());
    String responseString = response.getContentAsString();
    GenericResponse genericResponse = objectMapper.readValue(responseString,
      GenericResponse.class);
    assertEquals("expired token", genericResponse.getError());
    assertNotNull(response.getContentAsString());

    ApplicationUser user1 = userRepository.findUserByEmail("test@test.com");
    assertFalse(passwordEncoder.matches("password", user1.getPassword()));
  }
}

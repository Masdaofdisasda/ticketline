package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PageDtoResponse;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class EventEndpointTest implements TestData {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EventMapper eventMapper;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private SecurityProperties securityProperties;

  @BeforeEach
  void setUp() {
    eventRepository.deleteAll();
  }

  @Test
  void findAll_shouldReturnEmptyPage() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("pageIndex", "0");
    requestParams.add("pageSize", "10");

    MvcResult mvcResult = this.mockMvc.perform(get(EVENT_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .params(requestParams))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    PageDtoResponse<EventDto> pageDtoResponse = objectMapper.readValue(response.getContentAsString(), PageDtoResponse.class);

    assertEquals(0, pageDtoResponse.getPageIndex());
    assertEquals(10, pageDtoResponse.getPageSize());
    assertEquals(0, pageDtoResponse.getHits());
    assertEquals(0, pageDtoResponse.getPagesTotal());
    assertEquals(0, pageDtoResponse.getData().size());
  }

  @Test
  void filter_withQueryParams_shouldReturnOneHit() throws Exception {
    // prerequisite two event entities
    eventRepository.save(Event.builder()
      .id(1L)
      .category("HipHop")
      .name("Test Event")
      .startDate(LocalDateTime.now().plusMinutes(10))
      .endDate(LocalDateTime.now().plusDays(7))
      .build());

    eventRepository.save(Event.builder()
      .id(2L)
      .category("Klassik")
      .name("Test Event 2")
      .startDate(LocalDateTime.now().plusMinutes(10))
      .endDate(LocalDateTime.now().plusDays(7))
      .build());

    assertThat(eventRepository.findAll().size()).isEqualTo(2);

    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("artistName", "");
    requestParams.add("country", null);
    requestParams.add("city", null);
    requestParams.add("street", null);
    requestParams.add("zipCode", null);
    requestParams.add("venueName", null);
    requestParams.add("eventHall", null);
    requestParams.add("from", null);
    requestParams.add("tonight", "false");
    requestParams.add("startTime", null);
    requestParams.add("category", "hip");
    requestParams.add("nameOfEvent", null);
    requestParams.add("pageIndex", "0");
    requestParams.add("pageSize", "10");

    MvcResult mvcResult = this.mockMvc.perform(get(EVENT_BASE_URI + "/filter")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        .params(requestParams))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    PageDtoResponse<EventDto> pageDtoResponse = objectMapper.readValue(response.getContentAsString(), PageDtoResponse.class);

    assertEquals(0, pageDtoResponse.getPageIndex());
    assertEquals(10, pageDtoResponse.getPageSize());
    assertEquals(1, pageDtoResponse.getHits());
    assertEquals(1, pageDtoResponse.getPagesTotal());
    assertEquals(1, pageDtoResponse.getData().size());
  }

  @Test
  public void createEvent() throws Exception {
    EventDto eventDto1 = new EventDto(1L, "NewEvent", "newCategory", LocalDateTime.now(), LocalDateTime.now().plusHours(3), null);
    EventDto eventDto2 = new EventDto(2L, "NewEvent2", "newCategory2", LocalDateTime.now(), LocalDateTime.now().plusHours(3), null);
    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto1)
        )).andExpect(status().isCreated())
      .andReturn().getResponse().getContentAsByteArray();

    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto2)
        )).andExpect(status().isCreated())
      .andReturn().getResponse().getContentAsByteArray();

    assertThat(eventRepository.findAll().size()).isEqualTo(2);
  }

  @Test
  void findTopOfMonth() {
    // TODO: 11.12.22 add your testcase here
  }
}
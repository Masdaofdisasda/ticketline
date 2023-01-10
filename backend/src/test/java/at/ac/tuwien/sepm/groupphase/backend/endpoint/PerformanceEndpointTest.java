package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.ArtistFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.EventFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.RoomFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.TicketFixture;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoomMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PerformanceEndpointTest {
  private static final LocalDateTime start = LocalDateTime.of(2022, 1, 1, 10, 10, 10),
    end = LocalDateTime.of(2022, 1, 3, 10, 10, 10);
  @Autowired
  private PerformanceRepository performanceRepository;
  @Autowired
  private TicketMapper ticketMapper;
  @Autowired
  private ArtistMapper artistMapper;
  @Autowired
  private EventMapper eventMapper;
  @Autowired
  private RoomMapper roomMapper;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private JwtTokenizer jwtTokenizer;
  @Autowired
  private SecurityProperties securityProperties;

  @BeforeEach
  void clearRepository() {
    //performanceRepository.deleteAll();
  }

  @Test
  public void getById_shouldBe404() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get(TestData.PERFORMANCE_BASE_URI + "/10")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TestData.ADMIN_USER, TestData.ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  public void getAll_shouldReturnList() throws Exception {
    final Performance performance = Performance.builder()
      .startDate(start)
      .endDate(end)
      .artist(ArtistFixture.buildArtist1())
      .room(RoomFixture.buildRoom1())
      .event(EventFixture.buildEvent1())
      .tickets(List.of(
        TicketFixture.buildTicket1(),
        TicketFixture.buildTicket2()
      ))
      .build();

    long id = performanceRepository.save(performance).getId();

    MvcResult mvcResult = this.mockMvc.perform(get(TestData.PERFORMANCE_BASE_URI + "/" + id)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TestData.ADMIN_USER, TestData.ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    PerformanceDto performanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceDto.class);

    assertEquals(id, performanceDto.getId());
    assertEquals(start, performanceDto.getStartDate());
    assertEquals(end, performanceDto.getEndDate());
    assertNotNull(performanceDto.getTickets());
    assertEquals(2, performanceDto.getTickets().size());
    assertEquals(ticketMapper.ticketToTicketDto(TicketFixture.buildTicket1()), performanceDto.getTickets().get(0));
    assertEquals(ticketMapper.ticketToTicketDto(TicketFixture.buildTicket2()), performanceDto.getTickets().get(1));
    assertEquals(artistMapper.artistToArtistDto(ArtistFixture.buildArtist1()), performanceDto.getArtist());
    assertEquals(roomMapper.roomToRommDto(RoomFixture.buildRoom1()), performanceDto.getRoom());
  }


}

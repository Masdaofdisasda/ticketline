package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.ArtistFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.EventFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.PriceCategoryFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.RoomFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.SectorFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.VenueFixture;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDtoSimple;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceRoomDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoomMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PerformanceEndpointTest {

  private static final LocalDateTime start = LocalDateTime.of(2022, 1, 1, 10, 10, 10),
    end = LocalDateTime.of(2022, 1, 3, 10, 10, 10);
  @Autowired
  PriceCategoryRepository priceCategoryRepository;
  @Autowired
  ArtistRepository artistRepository;
  @Autowired
  PricingRepository pricingRepository;
  @Autowired
  RoomRepository roomRepository;
  @Autowired
  TicketRepository ticketRepository;
  @Autowired
  VenueRepository venueRepository;
  @Autowired
  EventRepository eventRepository;
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
  private PriceCategoryFixture priceCategoryFixture;
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

  @Test
  public void getPerformanceDtoSimple_shouldReturn() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;

    venueRepository.save(VenueFixture.buildVenue1());

    final Performance performance = Performance.builder()
      .id(1L)
      .event(eventRepository.save(EventFixture.buildEvent1()))
      .startDate(start)
      .endDate(end)
      .artists(new HashSet<>(artistRepository.saveAll(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2()))))
      .room(roomRepository.save(RoomFixture.buildRoom1()))
      .event(EventFixture.buildEvent1())
      .build();

    long id = performanceRepository.save(performance).getId();

    MvcResult mvcResult = this.mockMvc.perform(get(TestData.PERFORMANCE_BASE_URI + "/" + id)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TestData.ADMIN_USER, TestData.ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    PerformanceDtoSimple performanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceDtoSimple.class);

    assertEquals(id, performanceDto.getId());
    assertEquals(start, performanceDto.getStartDate());
    assertEquals(end, performanceDto.getEndDate());
    assertThat(artistMapper.artistsToArtistDtos(
      List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2()))
      .stream().sorted(Comparator.comparing(ArtistDto::getId)).toList())
      .isEqualTo(performanceDto.getArtists().stream().sorted(Comparator.comparing(ArtistDto::getId)).toList());
  }

  @Test
  @Transactional
  public void getPerformanceRoom_shouldReturn() throws Exception {
    var priceCategories = Arrays.asList(priceCategoryFixture.getAll());
    priceCategoryRepository.saveAll(priceCategories);
    SectorFixture.repository = priceCategoryRepository;

    venueRepository.save(VenueFixture.buildVenue1());
    final Room room = roomRepository.save(RoomFixture.buildRoom1());
    final List<Ticket> tickets = new ArrayList<>();

    final Performance performance = Performance.builder()
      .id(1L)
      .event(eventRepository.save(EventFixture.buildEvent1()))
      .startDate(start)
      .endDate(end)
      .artists(new HashSet<>(artistRepository.saveAll(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2()))))
      .room(room)
      .event(EventFixture.buildEvent1())
      .build();

    performanceRepository.save(performance);

    List<Pricing> pricings = new ArrayList<>();
    priceCategoryRepository.findAll().forEach(pc -> {
      Pricing pricing = pc.getPricings().stream().findFirst().orElseThrow();
      pricing.setPerformance(performance);
      pricings.add(pricing);
    });

    pricingRepository.saveAll(pricings);

    room.getSectors().forEach(sector -> {
      sector.getSeats().forEach(seat -> {
        Ticket ticket = Ticket.builder()
          .price(pricings.stream().filter(pricing -> pricing.getPerformance().getId()
            .equals(performance.getId())).findFirst().orElseThrow().getPricing())
          .performance(performance)
          .seat(seat)
          .build();
        tickets.add(ticket);
      });
    });

    ticketRepository.saveAll(tickets);

    long id = performanceRepository.save(performance).getId();

    MvcResult mvcResult = this.mockMvc.perform(get(TestData.PERFORMANCE_BASE_URI + "/" + id)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TestData.ADMIN_USER, TestData.ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    PerformanceRoomDto performanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceRoomDto.class);

    assertEquals(id, performanceDto.getId());
    assertNotNull(performanceDto.getRoom());
    assertEquals(room.getId(), performanceDto.getRoom().getId());
    assertEquals(room.getName(), performanceDto.getRoom().getName());
  }
}

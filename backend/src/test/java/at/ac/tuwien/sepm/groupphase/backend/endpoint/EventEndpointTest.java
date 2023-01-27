package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.ArtistFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.PriceCategoryFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.RoomFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.SectorFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.VenueFixture;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PageDtoResponse;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class EventEndpointTest implements TestData {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ArtistMapper artistMapper;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private SecurityProperties securityProperties;
  @Autowired
  private PriceCategoryFixture priceCategoryFixture;
  @Autowired
  private PricingRepository pricingRepository;
  @Autowired
  private PerformanceRepository performanceRepository;
  @Autowired
  private RoomRepository roomRepository;
  @Autowired
  private ArtistRepository artistRepository;
  @Autowired
  private PriceCategoryRepository priceCategoryRepository;
  @Autowired
  private VenueRepository venueRepository;
  @Autowired
  private EventRepository eventRepository;
  @Autowired
  private LocalContainerEntityManagerFactoryBean entityManager;

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

  @Transactional
  void savePerformances() {
    List<PriceCategory> priceCategories = Arrays.asList(priceCategoryFixture.getAll());
    priceCategoryRepository.saveAll(priceCategories);
    priceCategories.forEach(pc -> {
      pc.setPricings(Set.of(
        Pricing.builder()
          .priceCategory(pc)
          .pricing(BigDecimal.valueOf(32))
          .build(),
        Pricing.builder()
          .priceCategory(pc)
          .pricing(BigDecimal.valueOf(64))
          .build())
      );
    });

    SectorFixture.repository = priceCategoryRepository;
    venueRepository.save(VenueFixture.buildVenue1());
    final Room room = RoomFixture.buildRoom1();
    roomRepository.save(room);

    final Performance performance = Performance.builder()
      .startDate(LocalDateTime.now().plusHours(25))
      .endDate(LocalDateTime.now().plusHours(31))
      .artists(new HashSet<>(artistRepository.saveAll(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2()))))
      .room(room)
      .build();

    List<Pricing> pricings = new ArrayList<>();
    priceCategories.forEach(pc -> {
      Pricing pricing = pc.getPricings().stream().findFirst().orElseThrow();
      pricing.setPerformance(performance);
      pricing.setPriceCategory(pc);
      pricings.add(pricing);
    });


    final Performance performance1 = Performance.builder()
      .startDate(LocalDateTime.now().plusHours(25))
      .endDate(LocalDateTime.now().plusHours(31))
      .artists(new HashSet<>(artistRepository.saveAll(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2()))))
      .room(room)
      .build();

    performanceRepository.saveAll(List.of(performance, performance1));

    priceCategories.forEach(pc -> {
      Pricing pricing = pc.getPricings().stream().findFirst().orElseThrow();
      pricing.setPerformance(performance);
      pricing.setPriceCategory(pc);
      pricings.add(pricing);
    });

    pricingRepository.saveAll(pricings);

    // prerequisite two event entities
    Event event1 = eventRepository.save(Event.builder()
      .id(1L)
      .category("HipHop")
      .name("Test Event")
      .startDate(LocalDateTime.now().plusDays(1))
      .endDate(LocalDateTime.now().plusDays(4))
      .performances(Set.of(performance))
      .build());

    performance.setEvent(event1);

    Event event2 = eventRepository.save(Event.builder()
      .id(2L)
      .category("Klassik")
      .name("Test Event 2")
      .startDate(LocalDateTime.now().plusDays(1))
      .endDate(LocalDateTime.now().plusDays(6))
      .performances(Set.of(performance1))
      .build());

    performance1.setEvent(event2);

    eventRepository.saveAll(List.of(event1, event2));
  }

  @Test
  void filter_withQueryParams_shouldReturnOneHit() throws Exception {
    savePerformances();
    assertThat(eventRepository.findAll().size()).isEqualTo(2);

    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("artistName", "");
    requestParams.add("country", "");
    requestParams.add("city", "");
    requestParams.add("street", "");
    requestParams.add("zipCode", "");
    requestParams.add("venueName", "");
    requestParams.add("eventHall", "");
    requestParams.add("startTime", null);
    requestParams.add("endTime", null);
    requestParams.add("category", "hip");
    requestParams.add("nameOfEvent", "");
    requestParams.add("pageIndex", "0");
    requestParams.add("pageSize", "10");

    MvcResult mvcResult = this.mockMvc.perform(get(EVENT_BASE_URI + "/filter")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
        .params(requestParams))
      .andExpect(status().is2xxSuccessful())
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
  @Transactional
  public void createEvent() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;
    venueRepository.save(VenueFixture.buildVenue1());
    venueRepository.save(VenueFixture.buildVenue2());
    venueRepository.save(VenueFixture.buildVenue3());
    final Room room = RoomFixture.buildRoom7();
    roomRepository.save(room);

    ArtistDto artistDto1 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(1L, "Artist1", null)));
    ArtistDto artistDto2 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(2L, "Artist2", null)));

    PerformanceCreateDto performanceDto1 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(1),
      LocalDateTime.now().plusHours(2),
      List.of(artistDto1),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    PerformanceCreateDto performanceDto2 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(2),
      LocalDateTime.now().plusHours(3),
      List.of(artistDto1, artistDto2),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    PerformanceCreateDto performanceDto3 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(1),
      LocalDateTime.now().plusHours(2),
      List.of(artistDto2),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    PerformanceCreateDto performanceDto4 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(2),
      LocalDateTime.now().plusHours(3),
      List.of(artistDto2, artistDto1),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    EventCreateDto eventDto1 =
      new EventCreateDto(1L, "NewEvent", "newCategory", LocalDateTime.now(), LocalDateTime.now().plusHours(5), List.of(performanceDto1, performanceDto2));
    EventCreateDto eventDto2 =
      new EventCreateDto(2L, "NewEvent2", "newCategory2", LocalDateTime.now(), LocalDateTime.now().plusHours(5), List.of(performanceDto3, performanceDto4));
    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto1)
        )).andExpect(status().isCreated())
      .andReturn().getResponse().getContentAsByteArray();

    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto2)
        )).andExpect(status().isCreated())
      .andReturn().getResponse().getContentAsByteArray();

    List<Event> events = eventRepository.findAll();
    assertThat(events.size()).isEqualTo(2);
    assertThat(events.get(0).getName()).isEqualTo(eventDto1.getName());
    assertThat(events.get(0).getPerformances().size()).isEqualTo(2);
    assertThat(events.get(0).getStartDate().toLocalDate()).isEqualTo(eventDto1.getStartDate().toLocalDate());
    assertThat(events.get(0).getEndDate().toLocalDate()).isEqualTo(eventDto1.getEndDate().toLocalDate());
    assertThat(events.get(0).getName()).isEqualTo(eventDto1.getName());
    assertThat(events.get(0).getPerformances().size()).isEqualTo(eventDto1.getPerformances().size());
    assertThat(events.get(0).getId()).isEqualTo(eventDto1.getId());
    assertThat(events.get(1).getName()).isEqualTo(eventDto2.getName());
    assertThat(events.get(1).getPerformances().size()).isEqualTo(2);
    assertThat(events.get(1).getStartDate().toLocalDate()).isEqualTo(eventDto2.getStartDate().toLocalDate());
    assertThat(events.get(1).getEndDate().toLocalDate()).isEqualTo(eventDto2.getEndDate().toLocalDate());
    assertThat(events.get(1).getName()).isEqualTo(eventDto2.getName());
    assertThat(events.get(1).getPerformances().size()).isEqualTo(eventDto2.getPerformances().size());
    assertThat(events.get(1).getId()).isEqualTo(eventDto2.getId());
  }

  @Test
  public void createWithOverlapping_shouldThrowValidationError() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;
    venueRepository.save(VenueFixture.buildVenue1());
    venueRepository.save(VenueFixture.buildVenue2());
    venueRepository.save(VenueFixture.buildVenue3());
    final Room room = RoomFixture.buildRoom7();
    roomRepository.save(room);

    ArtistDto artistDto1 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(1L, "Artist1", null)));
    ArtistDto artistDto2 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(2L, "Artist2", null)));

    PerformanceCreateDto performanceDto1 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(1),
      LocalDateTime.now().plusHours(3),
      List.of(artistDto1),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    PerformanceCreateDto performanceDto2 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(2),
      LocalDateTime.now().plusHours(5),
      List.of(artistDto1, artistDto2),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    EventCreateDto eventDto1 =
      new EventCreateDto(1L, "NewEvent", "newCategory", LocalDateTime.now(), LocalDateTime.now().plusHours(5), List.of(performanceDto1, performanceDto2));
    String response = mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto1)
        )).andExpect(status().isUnprocessableEntity())
      .andReturn().getResponse().getContentAsString();

    assertThat(response).contains("overlaps");
  }

  @Test
  public void createWithoutPerformance_shouldThrowValidationError() throws Exception {
    EventCreateDto eventDto1 =
      new EventCreateDto(1L, "NewEvent", "newCategory", LocalDateTime.now(), LocalDateTime.now().plusHours(5), List.of());
    String response = mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto1)
        )).andExpect(status().isUnprocessableEntity())
      .andReturn().getResponse().getContentAsString();

    assertThat(response).contains("has to have at least one");
  }

  @Test
  public void createTwoWithSameName_shouldThrowValidationError() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;
    venueRepository.save(VenueFixture.buildVenue1());
    venueRepository.save(VenueFixture.buildVenue2());
    venueRepository.save(VenueFixture.buildVenue3());
    final Room room = RoomFixture.buildRoom7();
    roomRepository.save(room);

    ArtistDto artistDto1 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(1L, "Artist1", null)));
    ArtistDto artistDto2 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(2L, "Artist2", null)));

    PerformanceCreateDto performanceDto1 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(1),
      LocalDateTime.now().plusHours(2),
      List.of(artistDto1),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    PerformanceCreateDto performanceDto2 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(2),
      LocalDateTime.now().plusHours(3),
      List.of(artistDto1, artistDto2),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    PerformanceCreateDto performanceDto3 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(1),
      LocalDateTime.now().plusHours(2),
      List.of(artistDto2),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    PerformanceCreateDto performanceDto4 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(2),
      LocalDateTime.now().plusHours(3),
      List.of(artistDto2, artistDto1),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    EventCreateDto eventDto1 =
      new EventCreateDto(1L, "NewEvent", "newCategory", LocalDateTime.now(), LocalDateTime.now().plusHours(5), List.of(performanceDto1, performanceDto2));
    EventCreateDto eventDto2 =
      new EventCreateDto(2L, "NewEvent", "newCategory2", LocalDateTime.now(), LocalDateTime.now().plusHours(5), List.of(performanceDto3, performanceDto4));
    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto1)
        )).andExpect(status().isCreated())
      .andReturn().getResponse().getContentAsByteArray();

    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto2)
        )).andExpect(status().isUnprocessableEntity())
      .andReturn().getResponse().getContentAsByteArray();
  }

  @Test
  public void createPerformanceEndBeforeStart_shouldThrowValidationError() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;
    venueRepository.save(VenueFixture.buildVenue1());
    venueRepository.save(VenueFixture.buildVenue2());
    venueRepository.save(VenueFixture.buildVenue3());
    final Room room = RoomFixture.buildRoom7();
    roomRepository.save(room);

    ArtistDto artistDto1 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(1L, "Artist1", null)));

    PerformanceCreateDto performanceDto1 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(5),
      LocalDateTime.now().plusHours(2),
      List.of(artistDto1),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    EventCreateDto eventDto1 =
      new EventCreateDto(1L, "NewEvent", "newCategory", LocalDateTime.now(), LocalDateTime.now().plusHours(5), List.of(performanceDto1));
    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto1)
        )).andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void createEventEndBeforeStart_shouldThrowValidationError() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;
    venueRepository.save(VenueFixture.buildVenue1());
    venueRepository.save(VenueFixture.buildVenue2());
    venueRepository.save(VenueFixture.buildVenue3());
    final Room room = RoomFixture.buildRoom7();
    roomRepository.save(room);

    ArtistDto artistDto1 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(1L, "Artist1", null)));

    PerformanceCreateDto performanceDto1 = new PerformanceCreateDto(null,
      LocalDateTime.now().plusHours(1),
      LocalDateTime.now().plusHours(2),
      List.of(artistDto1),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    EventCreateDto eventDto1 =
      new EventCreateDto(1L, "NewEvent", "newCategory", LocalDateTime.now(), LocalDateTime.now().minusHours(5), List.of(performanceDto1));
    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto1)
        )).andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void createPerformanceStartBeforeEventStart_shouldThrowValidationError() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;
    venueRepository.save(VenueFixture.buildVenue1());
    venueRepository.save(VenueFixture.buildVenue2());
    venueRepository.save(VenueFixture.buildVenue3());
    final Room room = RoomFixture.buildRoom7();
    roomRepository.save(room);

    ArtistDto artistDto1 = artistMapper.artistToArtistDto(artistRepository.save(new Artist(1L, "Artist1", null)));

    PerformanceCreateDto performanceDto1 = new PerformanceCreateDto(null,
      LocalDateTime.now().minusHours(5),
      LocalDateTime.now().plusHours(2),
      List.of(artistDto1),
      1L,
      null,
      Collections.singletonMap(2L, BigDecimal.ZERO),
      List.of());
    EventCreateDto eventDto1 =
      new EventCreateDto(1L, "NewEvent", "newCategory", LocalDateTime.now(), LocalDateTime.now().plusHours(5), List.of(performanceDto1));
    mockMvc
      .perform(MockMvcRequestBuilders
        .post(EVENT_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, List.of("ROLE_ADMIN", "ROLE_USER")))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventDto1)
        )).andExpect(status().isUnprocessableEntity());
  }

  @Test
  void findTopOfonth() {
    // TODO: 11.12.22 add your testcase here
  }
}
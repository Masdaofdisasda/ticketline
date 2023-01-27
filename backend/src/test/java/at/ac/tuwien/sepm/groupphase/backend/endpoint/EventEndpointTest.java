package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class EventEndpointTest implements TestData {

/*
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
    List<PriceCategory> priceCategories = Arrays.asList(priceCategoryFixture.getAll());
    priceCategories.forEach(pc -> {
      pc.setPricingList(List.of(
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
    priceCategoryRepository.saveAll(priceCategories);
    SectorFixture.repository = priceCategoryRepository;

    venueRepository.save(VenueFixture.buildVenue1());
    final Room room = roomRepository.save(RoomFixture.buildRoom1());
    final List<Ticket> tickets = new ArrayList<>();

    final Performance performance = Performance.builder()
      .startDate(LocalDateTime.now().plusHours(25))
      .endDate(LocalDateTime.now().plusHours(31))
      .artists(artistRepository.saveAll(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2())))
      .room(room)
      .tickets(tickets)
      .build();

    List<Pricing> pricings = new ArrayList<>();
    priceCategoryRepository.findAll().forEach(pc -> {
      Pricing pricing = pc.getPricingList().get(0);
      pricing.setPerformance(performance);
      pricings.add(pricing);
    });

    final Performance performance1 = Performance.builder()
      .startDate(LocalDateTime.now().plusHours(25))
      .endDate(LocalDateTime.now().plusHours(31))
      .artists(artistRepository.saveAll(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2())))
      .room(room)
      .tickets(tickets)
      .build();

    priceCategoryRepository.findAll().forEach(pc -> {
      Pricing pricing = pc.getPricingList().get(1);
      pricing.setPerformance(performance);
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
      .performances(List.of(performance))
      .build());

    performance.setEvent(event1);

    Event event2 = eventRepository.save(Event.builder()
      .id(2L)
      .category("Klassik")
      .name("Test Event 2")
      .startDate(LocalDateTime.now().plusDays(1))
      .endDate(LocalDateTime.now().plusDays(6))
      .performances(List.of(performance1))
      .build());

    performance1.setEvent(event2);

    performanceRepository.saveAll(List.of(performance, performance1));

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
  public void createEvent() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;
    Room room = RoomFixture.buildRoom7();
    // venueRepository.save(room.getVenue());
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
    Room room = RoomFixture.buildRoom7();
    // venueRepository.save(room.getVenue());
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
    Room room = RoomFixture.buildRoom7();
    // venueRepository.save(room.getVenue());
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
    Room room = RoomFixture.buildRoom7();
    // venueRepository.save(room.getVenue());
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
    Room room = RoomFixture.buildRoom7();
    // venueRepository.save(room.getVenue());
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
    Room room = RoomFixture.buildRoom7();
    // venueRepository.save(room.getVenue());
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

 */
}
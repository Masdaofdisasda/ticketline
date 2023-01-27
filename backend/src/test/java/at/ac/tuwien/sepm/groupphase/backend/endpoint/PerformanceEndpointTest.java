package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PerformanceEndpointTest {
  /*
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
      .artists(artistRepository.saveAll(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2())))
      .room(roomRepository.save(RoomFixture.buildRoom1()))
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

    PerformanceDtoSimple performanceDto = objectMapper.readValue(response.getContentAsString(), PerformanceDtoSimple.class);

    assertEquals(id, performanceDto.getId());
    assertEquals(start, performanceDto.getStartDate());
    assertEquals(end, performanceDto.getEndDate());
    assertEquals(artistMapper.artistsToArtistDtos(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2())), performanceDto.getArtists());
  }

  @Test
  public void getPerformanceRoom_shouldReturn() throws Exception {
    priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
    SectorFixture.repository = priceCategoryRepository;

    venueRepository.save(VenueFixture.buildVenue1());
    final Room room = roomRepository.save(RoomFixture.buildRoom1());
    final List<Ticket> tickets = new ArrayList<>();

    final Performance performance = Performance.builder()
      .id(1L)
      .event(eventRepository.save(EventFixture.buildEvent1()))
      .startDate(start)
      .endDate(end)
      .artists(artistRepository.saveAll(List.of(ArtistFixture.buildArtist1(), ArtistFixture.buildArtist2())))
      .room(room)
      .event(EventFixture.buildEvent1())
      .tickets(tickets)
      .build();


    List<Pricing> pricings = new ArrayList<>();
    priceCategoryRepository.findAll().forEach(pc -> {
      Pricing pricing = pc.getPricingList().get(0);
      pricing.setPerformance(performance);
      pricings.add(pricing);
    });

    pricingRepository.saveAll(pricings);

    for (int i = 0; i < 2; i++) {
      Ticket ticket = Ticket.builder()
        .price(pricings.stream().filter(pricing -> pricing.getPerformance().getId()
          .equals(performance.getId())).findFirst().orElseThrow().getPricing())
        .performance(performance)
        .build();
      tickets.add(ticket);
    }
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

    assertNotNull(performanceDto.getTickets());
    assertEquals(tickets.size(), performanceDto.getTickets().size());
    assertEquals(tickets.get(0).getPrice(), performanceDto.getTickets().get(0).getPrice());
    assertEquals(tickets.get(1).getPrice(), performanceDto.getTickets().get(1).getPrice());
  }


   */

}

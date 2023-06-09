package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class PerformanceMapperTest {
/*
  @Autowired
  private PerformanceMapper mapper;

  Ticket ticket = Ticket.builder()
    .id(10L)
    .build();

  Performance performance = Performance.builder()
    .id(0L)
    .room(Room.builder().build())
    .startDate(LocalDateTime.MIN)
    .endDate(LocalDateTime.MAX)
    //.tickets(List.of(ticket))
    .build();

  TicketDto ticketDto = TicketDto.builder()
    .id(10L)
    .build();

  PerformanceDto performanceDto = PerformanceDto.builder()
    .id(1L)
    //.room(RoomDto.builder().sectors(new ArrayList<>()).build())
    .startDate(LocalDateTime.MAX)
    .startDate(LocalDateTime.now())
    .tickets(List.of(ticketDto))
    .build();

  @Test
  void performanceDtoToPerformance() {
    Performance currentPerformance = mapper.performanceDtoToPerformance(performanceDto);

    assertAll(() -> {
      assertThat(currentPerformance.getId()).isEqualTo(performanceDto.getId());
      assertThat(currentPerformance.getStartDate()).isEqualTo(performanceDto.getStartDate());
      assertThat(currentPerformance.getEndDate()).isEqualTo(performanceDto.getEndDate());
      //assertThat(currentPerformance.getTickets()).hasSameSizeAs(List.of(ticket));
    });
  }

  @Test
  void performanceToPerformanceDto() {
    PerformanceDto currentDto = mapper.performanceToPerformanceDto(performance);

    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(performance.getId());
      assertThat(currentDto.getStartDate()).isEqualTo(performance.getStartDate());
      assertThat(currentDto.getEndDate()).isEqualTo(performance.getEndDate());
      assertThat(currentDto.getTickets()).isEqualTo(List.of(ticketDto));
    });
  }

 */
}
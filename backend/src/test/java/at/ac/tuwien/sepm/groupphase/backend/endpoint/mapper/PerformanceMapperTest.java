package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class PerformanceMapperTest {

  @Autowired
  private PerformanceMapper mapper;

  Ticket ticket = Ticket.builder()
    .id(10L)
    .build();

  Performance performance = Performance.builder()
    .id(0L)
    .startDate(LocalDateTime.MIN)
    .endDate(LocalDateTime.MAX)
    .tickets(List.of(ticket))
    .build();

  TicketDto ticketDto = TicketDto.builder()
    .id(10L)
    .build();

  PerformanceDto performanceDto = PerformanceDto.builder()
    .id(1L)
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
      assertThat(currentPerformance.getTickets()).hasSameSizeAs(List.of(ticket));
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
}
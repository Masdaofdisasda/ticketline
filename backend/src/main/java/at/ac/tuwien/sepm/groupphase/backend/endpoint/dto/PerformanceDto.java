package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDto {
  private Long id;
  private LocalDate startDate;
  private LocalDate endDate;

  @Builder.Default
  private List<TicketDto> tickets = new ArrayList<>();
}

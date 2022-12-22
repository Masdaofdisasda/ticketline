package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDto {
  private Long id;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  @Builder.Default
  private List<TicketDto> tickets = new ArrayList<>();
}

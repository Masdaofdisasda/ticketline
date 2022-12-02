package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

  private Long id;
  private String name;
  private String category;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<PerformanceDto> performances;
}

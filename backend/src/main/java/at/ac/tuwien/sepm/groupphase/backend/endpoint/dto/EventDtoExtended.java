package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EventDtoExtended {
  Long id;
  String name;
  String category;
  LocalDateTime startDate;
  LocalDateTime endDate;
  List<PerformanceDtoSimple> performances;
  private String artistName;
  private String venueName;
  private String eventHallName;
}

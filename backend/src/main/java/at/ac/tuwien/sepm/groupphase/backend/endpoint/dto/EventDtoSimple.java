package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EventDtoSimple {
  Long id;
  String name;
  String category;
  LocalDateTime startDate;
  LocalDateTime endDate;
  String artistName;
  String venueName;
  String eventHallName;

  List<PerformanceDtoSimple> performances;
}

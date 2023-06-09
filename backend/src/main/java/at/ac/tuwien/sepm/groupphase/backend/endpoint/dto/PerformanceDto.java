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
  Long id;
  LocalDateTime startDate;
  LocalDateTime endDate;
  List<ArtistDto> artists = new ArrayList<>();
  RoomDto room;
  @Builder.Default
  List<TicketDto> tickets = new ArrayList<>();
}

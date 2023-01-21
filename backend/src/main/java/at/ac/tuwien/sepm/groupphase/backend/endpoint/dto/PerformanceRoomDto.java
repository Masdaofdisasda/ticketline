package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceRoomDto {
  Long id;
  RoomDto room;
  @Builder.Default
  List<TicketSimpleDto> tickets = new ArrayList<>();
}

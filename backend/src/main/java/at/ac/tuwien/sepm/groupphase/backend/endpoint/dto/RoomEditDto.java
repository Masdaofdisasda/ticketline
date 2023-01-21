package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RoomEditDto {
  private Long id;
  private String name;
  private Integer rowSize;
  private Integer columnSize;

  private List<SectorEditDto> sectors;
}

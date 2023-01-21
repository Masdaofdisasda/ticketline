package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoomAddDto {
  @NotNull
  private String name;

  @NotNull
  private Integer columnSize;
  @NotNull
  private Integer rowSize;

  @NotNull
  private List<SectorAddDto> sectors;
}

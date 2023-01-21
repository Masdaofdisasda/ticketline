package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A DTO for the {@link at.ac.tuwien.sepm.groupphase.backend.entity.Room} entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
  private Long id;
  private String name;
  private Integer columnSize;
  private Integer rowSize;
  private List<SectorDto> sectors;
}
package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan} entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatingPlanDto {
  private Long id;
  private int rows;
  private int columns;
  private RoomDto roomDto;
}
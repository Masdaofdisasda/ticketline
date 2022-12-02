package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link at.ac.tuwien.sepm.groupphase.backend.entity.Seat} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
  private Long id;
  private int rowNumber;
  private int colNumber;
}
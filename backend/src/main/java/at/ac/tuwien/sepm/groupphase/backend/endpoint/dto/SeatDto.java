package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link at.ac.tuwien.sepm.groupphase.backend.entity.Seat} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
  @NotNull
  private Long id;
  @NotNull
  private Integer rowNumber;
  @NotNull
  private Integer colNumber;
  @Nullable
  private String rowName;
  @Nullable
  private String colName;
  @NotNull
  private Seat.State state;
}
package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SeatAddDto {
  @NotNull
  private Integer rowNumber;
  @NotNull
  private Integer colNumber;
  @Nullable
  private String rowName;
  @Nullable
  private String colName;
  @Builder.Default
  private Seat.State state = Seat.State.UNSET;
}

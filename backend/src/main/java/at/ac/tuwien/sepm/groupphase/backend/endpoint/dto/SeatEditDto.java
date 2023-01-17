package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SeatEditDto {
  @Nullable
  private Long id;
  @Nullable
  private Integer rowNumber;
  @Nullable
  private Integer colNumber;
  @Nullable
  private String rowName;
  @Nullable
  private String colName;
}

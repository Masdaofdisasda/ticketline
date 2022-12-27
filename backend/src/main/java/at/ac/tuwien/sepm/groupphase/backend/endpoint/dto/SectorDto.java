package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorDto {
  @NotNull
  private Long id;
  @NotNull
  private String name;
  @NotNull
  private PriceCategoryDto priceCategory;
  @NotNull
  private List<SeatDto> seats;
}

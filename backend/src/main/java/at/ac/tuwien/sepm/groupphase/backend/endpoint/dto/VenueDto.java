package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueDto {
  @NotNull
  private Long id;

  @NotNull
  private String name;

  @NotNull
  private String street;

  @NotNull
  private String houseNumber;

  @NotNull
  private String city;

  @NotNull
  private String country;

  @NotNull
  private String zipCode;

  @NotNull
  private List<RoomDto> rooms;
}

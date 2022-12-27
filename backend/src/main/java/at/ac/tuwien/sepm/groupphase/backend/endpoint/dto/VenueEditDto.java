package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class VenueEditDto {
  @Nullable
  private String name;
  @Nullable
  private String street;
  @Nullable
  private String houseNumber;
  @Nullable
  private String city;
  @Nullable
  private String zipCode;
  @Nullable
  private String country;

  @Nullable
  private List<RoomEditDto> rooms;
}

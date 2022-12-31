package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserDto {
  @NotNull(message = "Email must not be null")
  @Email
  private String email;

  @NotNull(message = "First name must not be null")
  private String firstName;

  @NotNull(message = "Last name must not be null")
  private String lastName;

  @NotNull(message = "ID must not be null")
  private Long id;

  private Boolean accountNonLocked;
}

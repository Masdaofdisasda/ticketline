package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {
  @NotNull(message = "Token must not be null")
  private  String token;
  @NotNull(message = "newPassword must not be null")
  private String newPassword;
}

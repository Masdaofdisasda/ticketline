package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewsCreationDto {

  @NotNull(message = "Title must not be null")
  @Size(max = 100)
  private String title;

  @NotNull(message = "Summary must not be null")
  @Size(max = 500)
  private String summary;

  @NotNull(message = "Text must not be null")
  @Size(max = 10000)
  private String text;

  @NotNull
  private LocalDateTime publishedAt;

  private String fileName;
}
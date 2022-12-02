package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * A DTO for the {@link at.ac.tuwien.sepm.groupphase.backend.entity.News} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
  private Long id;
  private String title;
  private String summary;
  private String text;
  private LocalDate publishedAt;
  private String pictureUrl;
}
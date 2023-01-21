package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link at.ac.tuwien.sepm.groupphase.backend.entity.News} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsOverviewDto {
  private Long id;
  private String title;
  private String summary;
  private LocalDateTime publishedAt;
}
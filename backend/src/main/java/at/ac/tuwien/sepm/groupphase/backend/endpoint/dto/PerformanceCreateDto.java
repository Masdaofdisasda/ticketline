package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceCreateDto {
  Long id;
  LocalDateTime startDate;
  LocalDateTime endDate;
  List<ArtistDto> artists = new ArrayList<>();
  Long roomId;
  @Builder.Default
  List<TicketDto> tickets = new ArrayList<>();
  Map<Long, BigDecimal> priceCategoryPricingMap = new HashMap<>();
}

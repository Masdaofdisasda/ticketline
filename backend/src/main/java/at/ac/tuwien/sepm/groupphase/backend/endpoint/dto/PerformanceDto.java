package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDto {
  Long id;
  LocalDateTime startDate;
  LocalDateTime endDate;
  List<ArtistDto> artists = new ArrayList<>();
  RoomDto room;
  @Builder.Default
  List<TicketDto> tickets = new ArrayList<>();
  Map<Long, BigDecimal> priceCategoryPricingMap = new HashMap<>();
}

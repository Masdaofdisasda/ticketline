package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A DTO for the {@link Booking} entity.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
  private Long id;
}
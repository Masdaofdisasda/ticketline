package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A DTO for the {@link at.ac.tuwien.sepm.groupphase.backend.entity.Purchase} entity.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
  private Long id;
}
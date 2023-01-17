package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Pricing {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "performance_id")
  @ManyToOne(cascade = CascadeType.MERGE)
  @ToString.Exclude
  private Performance performance;

  @JoinColumn(name = "pricecategory_id")
  @ManyToOne(cascade = CascadeType.MERGE)
  @ToString.Exclude
  private PriceCategory priceCategory;

  private BigDecimal pricing;
}

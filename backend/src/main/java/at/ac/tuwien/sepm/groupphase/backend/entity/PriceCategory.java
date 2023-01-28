package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "PRICE_CATEGORY")
public class PriceCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 8)
  @Column(name = "COLOR", length = 8)
  private String color;

  @Size(max = 255)
  @Column(name = "NAME")
  private String name;

  @OneToMany(mappedBy = "priceCategory")
  private Set<Pricing> pricings = new LinkedHashSet<>();

  @OneToMany(mappedBy = "priceCategory")
  private Set<Sector> sectors = new LinkedHashSet<>();
}
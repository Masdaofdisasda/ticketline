package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriceCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @JoinTable(name = "pricing",
    joinColumns = @JoinColumn(name = "pricecategory_id"),
    inverseJoinColumns = @JoinColumn(name = "performance_id"))
  @Builder.Default
  private List<Performance> performances = new ArrayList<>();

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @Builder.Default
  private List<Sector> sector = new ArrayList<>();

  @OneToMany(mappedBy = "priceCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @Builder.Default
  private List<Pricing> pricingList = new ArrayList<>();

  @Size(min = 1, max = 255)
  private String name;
  @Size(min = 3, max = 8)
  private String color;

}

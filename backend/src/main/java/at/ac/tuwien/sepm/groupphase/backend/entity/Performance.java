package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Performance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  @OneToMany(mappedBy = "performance")
  @Builder.Default
  private List<Ticket> tickets = new ArrayList<>();

  @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL)
  private List<Pricing> pricing;

  @ManyToMany(mappedBy = "performances")
  private List<PriceCategory> priceCategories;

  @ManyToOne()
  private Event event;

  @ManyToOne()
  private Room room;

  @ManyToMany
  @Fetch(FetchMode.SUBSELECT)
  @JoinTable(
    name = "performace_artists",
    joinColumns = @JoinColumn(name = "perfromance_id"),
    inverseJoinColumns = @JoinColumn(name = "artist_id"))
  @ToString.Exclude
  private List<Artist> artists;
}

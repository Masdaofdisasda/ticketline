package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "SECTOR")
public class Sector {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 255)
  @NotNull
  @Column(name = "NAME", nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRICE_CATEGORY_ID")
  private PriceCategory priceCategory;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROOM_ID")
  private Room room;

  @Builder.Default
  @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL)
  private Set<Seat> seats = new LinkedHashSet<>();


  public void addSeat(Seat s) {
    this.seats.add(s);
  }
}
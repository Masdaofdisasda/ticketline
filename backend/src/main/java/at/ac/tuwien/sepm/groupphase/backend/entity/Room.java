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
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ROOM")
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "COLUMN_SIZE")
  private Integer columnSize;

  @Size(max = 255)
  @Column(name = "NAME")
  private String name;

  @Column(name = "ROW_SIZE")
  private Integer rowSize;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VENUE_ID")
  private Venue venue;

  @OneToMany(mappedBy = "room")
  private Set<Performance> performances = new LinkedHashSet<>();

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
  private Set<Sector> sectors = new LinkedHashSet<>();

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
  private Set<SeatingPlan> seatingPlans = new LinkedHashSet<>();
}
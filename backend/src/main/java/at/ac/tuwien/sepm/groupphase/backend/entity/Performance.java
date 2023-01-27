package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "PERFORMANCE")
public class Performance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "END_DATE")
  private LocalDateTime endDate;

  @Column(name = "START_DATE")
  private LocalDateTime startDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EVENT_ID")
  private Event event;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROOM_ID")
  private Room room;

  @ManyToMany
  @JoinTable(name = "PERFORMANCE_ARTISTS",
      joinColumns = @JoinColumn(name = "PERFORMANCE_ID"),
      inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"))
  private Set<Artist> artists = new LinkedHashSet<>();
}
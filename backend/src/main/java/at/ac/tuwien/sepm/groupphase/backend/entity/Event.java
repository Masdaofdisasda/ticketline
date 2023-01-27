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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "EVENT")
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 100)
  @NotNull
  @Column(name = "CATEGORY", nullable = false, length = 100)
  private String category;

  @Column(name = "END_DATE")
  private LocalDateTime endDate;

  @Size(max = 100)
  @NotNull
  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @Column(name = "START_DATE")
  private LocalDateTime startDate;

  @OneToMany(mappedBy = "event")
  private Set<Performance> performances = new LinkedHashSet<>();
}
package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Range;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Range(min = 0, max = 1024)
  private Integer columnSize;
  @Range(min = 0, max = 1024)
  private Integer rowSize;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "room", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @Builder.Default
  @NotNull
  private List<Sector> sectors = new ArrayList<>();

  @ManyToOne(cascade = CascadeType.ALL)
  private Venue venue;

  @OneToMany(mappedBy = "room")
  @Builder.Default
  private List<Performance> performances = new ArrayList<>();
}

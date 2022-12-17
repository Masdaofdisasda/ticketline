package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
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

  private LocalDate startDate;

  private LocalDate endDate;

  @OneToMany(mappedBy = "performance", fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @Builder.Default
  private List<Ticket> tickets = new ArrayList<>();

  @ManyToOne()
  private Event event;

  @ManyToOne
  private Artist artist;

  @ManyToOne
  private Location location;

  @Override
  public String toString() {
    return "Performance{" + "id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + "}";
  }
}

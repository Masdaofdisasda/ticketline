package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 100)
  private String category;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
  //MultipleBagFetchException: cannot simultaneously fetch multiple bags
  @Fetch(value = FetchMode.SUBSELECT)
  @Builder.Default
  private List<Performance> performances = new ArrayList<>();

  @OneToMany(mappedBy = "event", fetch = FetchType.EAGER) //todo: has to be changed at a later point
  @Fetch(value = FetchMode.SUBSELECT)
  @Builder.Default
  private List<News> news = new ArrayList<>();
}

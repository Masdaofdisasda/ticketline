package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Venue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(min = 1, max = 255)
  private String name;

  @Size(min = 1, max = 255)
  private String street;

  @Size(min = 1, max = 255)
  private String houseNumber;

  @Size(min = 1, max = 255)
  private String city;

  @Size(min = 1, max = 255)
  private String country;

  @Size(min = 1, max = 255)
  private String zipCode;

  @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
  @Fetch(FetchMode.JOIN)
  @Builder.Default
  private List<Room> rooms = new ArrayList<>();

}

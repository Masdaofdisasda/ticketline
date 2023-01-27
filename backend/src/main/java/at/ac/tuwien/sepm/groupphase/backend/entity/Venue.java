package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "VENUE")
public class Venue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 255)
  @Column(name = "CITY")
  private String city;

  @Size(max = 255)
  @Column(name = "COUNTRY")
  private String country;

  @Size(max = 255)
  @Column(name = "HOUSE_NUMBER")
  private String houseNumber;

  @Size(max = 255)
  @Column(name = "NAME")
  private String name;

  @Size(max = 255)
  @Column(name = "STREET")
  private String street;

  @Size(max = 255)
  @Column(name = "ZIP_CODE")
  private String zipCode;

  @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
  private Set<Room> rooms = new LinkedHashSet<>();

  public void addRoom(Room room) {
    rooms.add(room);
  }
}
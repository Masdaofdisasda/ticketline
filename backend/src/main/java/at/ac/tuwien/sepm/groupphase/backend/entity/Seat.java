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
@Table(name = "SEAT")
public class Seat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 16)
  @Column(name = "COL_NAME", length = 16)
  private String colName;

  @NotNull
  @Column(name = "COL_NUMBER", nullable = false)
  private Integer colNumber;

  @Size(max = 16)
  @Column(name = "ROW_NAME", length = 16)
  private String rowName;

  @NotNull
  @Column(name = "ROW_NUMBER", nullable = false)
  private Integer rowNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SECTOR_ID")
  private Sector sector;

  @OneToMany(mappedBy = "seat")
  private Set<Ticket> tickets = new LinkedHashSet<>();
}
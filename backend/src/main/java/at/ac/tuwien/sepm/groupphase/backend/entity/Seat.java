package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Seat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Integer rowNumber;

  @NotNull
  private Integer colNumber;

  @Length(min = 0, max = 16)
  private String rowName;
  @Length(min = 0, max = 16)
  private String colName;

  @Enumerated(EnumType.ORDINAL)
  private State state;

  @ManyToOne
  private Sector sector;

  @OneToOne
  private Ticket ticket;

  public enum State {
    FREE(),
    RESERVED(),
    TAKEN(),
    BLOCKED(),
    UNSET()
  }
}

package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private float price;

  @OneToOne(mappedBy = "ticket")
  private Seat seat;

  @ManyToOne
  private Performance performance;

  @ManyToOne
  private Booking booking;
}
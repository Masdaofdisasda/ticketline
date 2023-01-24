package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Builder(toBuilder = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal price;

  @ManyToOne
  private Seat seat;

  @ManyToOne
  private Performance performance;

  @ManyToOne(cascade = CascadeType.ALL)
  private Booking booking;

  @ManyToMany(mappedBy = "canceledTickets")
  private Set<Booking> canceledBookings = new HashSet<>();

  public void addCanceledBooking(Booking booking) {
    if (canceledBookings.contains(booking)) {
      return;
    }
    canceledBookings.add(booking);
    booking.addCanceledTicket(this);
  }


  private byte[] validationHash;

  @Builder.Default
  private Boolean used = false;

}

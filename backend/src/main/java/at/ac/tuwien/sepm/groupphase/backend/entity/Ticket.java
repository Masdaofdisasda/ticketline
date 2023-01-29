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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "TICKET")
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "PRICE", precision = 19, scale = 2)
  private BigDecimal price;

  @Column(name = "USED", nullable = false)
  @Builder.Default
  private boolean used = false;

  @Column(name = "VALIDATION_HASH")
  private byte[] validationHash;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOKING_ID")
  private Booking booking;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PERFORMANCE_ID")
  private Performance performance;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SEAT_ID")
  private Seat seat;

<<<<<<< Updated upstream
  @ManyToMany(mappedBy = "canceledTickets")
  private Set<Booking> canceledBookings = new LinkedHashSet<>();

  public void addCanceledBooking(Booking booking) {
    if (canceledBookings.contains(booking)) {
      return;
    }
    canceledBookings.add(booking);
    booking.addCanceledTicket(this);
  }
=======
  @ManyToMany
  @JoinTable(name = "BOOKING_CANCELED_TICKETS",
      joinColumns = @JoinColumn(name = "CANCELED_TICKETS_ID"),
      inverseJoinColumns = @JoinColumn(name = "CANCELED_BOOKINGS_ID"))
  private Set<Booking> bookings = new LinkedHashSet<>();
>>>>>>> Stashed changes
}
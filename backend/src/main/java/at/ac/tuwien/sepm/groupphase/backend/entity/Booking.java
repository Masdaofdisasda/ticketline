package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "BOOKING")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @NotNull
  @Enumerated
  @Column(name = "BOOKING_TYPE", nullable = false)
  private BookingType bookingType;

  @NotNull
  @Column(name = "CREATED_DATE", nullable = false)
  private LocalDateTime createdDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private ApplicationUser user;

  @OneToMany(mappedBy = "booking")
  private Set<Ticket> tickets = new LinkedHashSet<>();

  @ManyToMany(cascade = CascadeType.PERSIST)
  private Set<Ticket> canceledTickets = new LinkedHashSet<>();

  public BigDecimal calculateTotal() {
    return tickets.stream().map(Ticket::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal calculateCancelTotal() {
    return canceledTickets.stream().map(Ticket::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void addCanceledTicket(Ticket ticket) {
    if (canceledTickets.contains(ticket)) {
      return;
    }
    canceledTickets.add(ticket);
    ticket.addCanceledBooking(this);
  }

  public void removeTicket(Ticket ticket) {
    if (!tickets.contains(ticket)) {
      return;
    }
    tickets.remove(ticket);
    ticket.setBooking(null);
  }

  public void cancel() {
    while (!tickets.isEmpty()) {
      Ticket ticket = tickets.stream().findFirst().orElseThrow(() -> new NotFoundException("No tickets to cancel."));
      removeTicket(ticket);
      addCanceledTicket(ticket);
      setBookingType(BookingType.CANCELLATION);
    }
  }

  public void revoke() {
    user.removeBooking(this);
    while (!tickets.isEmpty()) {
      Ticket ticket = tickets.stream().findFirst().orElseThrow(() -> new NotFoundException("No tickets to revoke."));
      removeTicket(ticket);
    }
  }

  public void addTicket(Ticket ticket) {
    tickets.add(ticket);
  }
}
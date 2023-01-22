package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Builder(toBuilder = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Enumerated
  private BookingType bookingType;

  @NotNull
  @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
  @Fetch(FetchMode.JOIN)
  @Builder.Default
  private List<Ticket> tickets = new ArrayList<>();

  @ManyToMany
  @Fetch(FetchMode.JOIN)
  @Builder.Default
  private Set<Ticket> canceledTickets = new HashSet<>();

  @Transient
  public BigDecimal calculateTotal() {
    Stream<Ticket> tickets = getTickets().isEmpty()
      ? getCanceledTickets().stream()
      : getTickets().stream();

    return tickets
      .map(Ticket::getPrice)
      .toList().stream()
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @ManyToOne
  private ApplicationUser user;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdDate;

  public void addTicket(Ticket ticket) {
    if (tickets.contains(ticket)) {
      return;
    }
    tickets.add(ticket);
    ticket.setBooking(this);
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
      Ticket ticket = tickets.get(0);
      removeTicket(ticket);
      addCanceledTicket(ticket);
      setBookingType(BookingType.CANCELLATION);
    }
  }

  public void revoke() {
    user.removeBooking(this);
    while (!tickets.isEmpty()) {
      Ticket ticket = tickets.get(0);
      removeTicket(ticket);
    }
  }
}

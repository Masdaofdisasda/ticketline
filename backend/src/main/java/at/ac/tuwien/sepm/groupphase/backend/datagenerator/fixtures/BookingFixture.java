package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingFixture {

  private BookingFixture() {
    throw new UnsupportedOperationException();
  }

  public static List<Booking> getBuildBooking(int counter) {
    List<Booking> bookings = new ArrayList<>();
    for (int i = 1; i <= counter; i++) {
      switch (i % 3) {
        case 0 -> bookings.add(buildBooking1()); // do not modify this or the entity cannot be persisted
        case 1 -> bookings.add(buildBooking2());
        default -> bookings.add(buildBooking3());
      }
    }
    return bookings;
  }

  public static Booking buildBooking1() {
    Booking booking = Booking.builder()
      .bookingType(BookingType.PURCHASE)
      .createdDate(LocalDateTime.now())
      .build();

    List<Ticket> tickets = TicketFixture.getBuildTickets(4);
    tickets.forEach(booking::addTicket);

    return booking;
  }

  public static Booking buildBooking2() {
    Booking booking = Booking.builder()
      .bookingType(BookingType.RESERVATION)
      .createdDate(LocalDateTime.now())
      .build();

    List<Ticket> tickets = TicketFixture.getBuildTickets(3);
    tickets.forEach(booking::addTicket);

    return booking;
  }

  public static Booking buildBooking3() {
    Booking booking = Booking.builder()
      .bookingType(BookingType.PURCHASE)
      .createdDate(LocalDateTime.now())
      .build();

    List<Ticket> tickets = TicketFixture.getBuildTickets(1);
    tickets.forEach(booking::addTicket);

    return booking;
  }

}

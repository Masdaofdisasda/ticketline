package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingItemDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {

  public BookingItemDto map(Booking booking) {
    return BookingItemDto.builder()
      .id(booking.getId())
      .bookedOn(booking.getCreatedDate())
      .orderTotal(getTotal(booking))
      .type(booking.getBookingType())
      .build();
  }

  public BookingDetailDto map(Booking booking, String email) {
    return BookingDetailDto.builder()
      .bookingId(booking.getId())
      .boughtByEmail(email)
      .orderTotal(getTotal(booking))
      .type(booking.getBookingType())
      .build();
  }

  public List<BookingItemDto> map(List<Booking> bookings) {
    return bookings.stream().map(
      this::map
    ).toList();
  }

  private Float getTotal(Booking booking) {
    return booking.getTickets().stream()
      .map(Ticket::getPrice)
      .toList().stream()
      .reduce(0f, Float::sum);
  }

}

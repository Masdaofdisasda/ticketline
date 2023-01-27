package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingItemDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class BookingMapper {

  public BookingItemDto map(Booking booking) {
    return BookingItemDto.builder()
      .id(booking.getId())
      .orderTotal(booking.calculateTotal())
      .bookedOn(booking.getCreatedDate())
      .orderTotal(booking.calculateTotal())
      .type(booking.getBookingType())
      .build();
  }

  public BookingDetailDto map(Booking booking, String email) {
    return BookingDetailDto.builder()
      .bookingId(booking.getId())
      .orderTotal(booking.calculateTotal())
      .boughtByEmail(email)
      .type(booking.getBookingType())
      .build();
  }

  public List<BookingItemDto> map(Set<Booking> bookings) {
    return bookings.stream().map(
        this::map
    ).toList();
  }

}

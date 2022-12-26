package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BookingMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implements the Booking Service Interface used by the customer to book tickets.
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

  private final UserRepository userRepository;
  private final BookingMapper bookingMapper;

  @Override
  @Transactional
  public Long reserveTickets(Collection<TicketDto> tickets) {
    ensureSeatsAreAvailable(tickets);

    Booking booking = createBookingFromTickets(tickets, BookingType.RESERVATION);
    ApplicationUser user = attachBookingToUser(booking);

    return getLatestBooking(user);
  }

  @Override
  @Transactional
  public Long purchaseTickets(Collection<TicketDto> tickets) {
    ensureSeatsAreAvailable(tickets);

    Booking booking = createBookingFromTickets(tickets, BookingType.PURCHASE);
    ApplicationUser user = attachBookingToUser(booking);

    return getLatestBooking(user);
  }

  @Override
  public BookingDetailDto fetchBookingDetails(Long bookingId) {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    Optional<Booking> matchingBooking = user.getBookings()
      .stream()
      .filter(booking -> Objects.equals(booking.getId(), bookingId))
      .findFirst();

    Booking booking = matchingBooking.orElseThrow(() ->
      new NotFoundException("No booking with id " + bookingId + " found for current user")
    );

    return bookingMapper.map(booking, getEmail());
  }

  @Override
  public List<BookingItemDto> fetchBookings() {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    List<Booking> bookings = user.getBookings();

    return bookingMapper.map(bookings);
  }

  @Override
  @Transactional
  public void cancelBooking(Long bookingId) {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    Optional<Booking> matchingBooking = user.getBookings()
      .stream()
      .filter(booking -> Objects.equals(booking.getId(), bookingId))
      .findFirst();

    Booking booking = matchingBooking.orElseThrow(() ->
      new NotFoundException("No booking with id " + bookingId + " found for current user")
    );

    List<Booking> bookings = user.getBookings();
    bookings.removeIf(b -> b.equals(booking));
    user.setBookings(bookings);

    userRepository.save(user);
  }

  @Override
  @Transactional
  public void purchaseReservation(Long bookingId) {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    Optional<Booking> matchingBooking = user.getBookings()
      .stream()
      .filter(booking -> Objects.equals(booking.getId(), bookingId))
      .findFirst();

    Booking booking = matchingBooking.orElseThrow(() ->
      new NotFoundException("No booking with id " + bookingId + " found for current user")
    );

    booking.setBookingType(BookingType.PURCHASE);

    userRepository.save(user);
  }

  private static Long getLatestBooking(ApplicationUser user) {
    List<Booking> bookings = user.getBookings();

    LocalDateTime latestDate = bookings.stream()
      .map(Booking::getCreatedDate)
      .toList().stream().max(Comparator.naturalOrder())
      .orElseThrow(NotFoundException::new);

    Optional<Booking> latestBooking = bookings.stream()
      .filter(booking -> booking.getCreatedDate().equals(latestDate))
      .findFirst();

    return latestBooking.orElseThrow(NotFoundException::new).getId();
  }


  private ApplicationUser attachBookingToUser(Booking booking) {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    user.addBooking(booking);
    return userRepository.save(user);
  }

  private Booking createBookingFromTickets(Collection<TicketDto> tickets, BookingType type) {
    Booking booking = new Booking();
    List<Ticket> ticketList = tickets
      .stream().sequential()
      .map(ticketDto -> Ticket.builder().price(ticketDto.getPrice()).build()) //TODO: create mapper & extend Tickets fields
      .toList();

    ticketList.forEach(booking::addTicket);
    booking.setBookingType(type);
    booking.setCreatedDate(LocalDateTime.now());

    return booking;
  }

  private static String getEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
  }

  private void ensureSeatsAreAvailable(Collection<TicketDto> tickets) {
    //TODO fetch seats

    if (false) {
      throw new RuntimeException();
    }
  }
}

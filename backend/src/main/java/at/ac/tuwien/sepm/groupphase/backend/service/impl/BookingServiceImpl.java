package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketBookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BookingMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.CreatePdfService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketValidationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;
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
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final UserRepository userRepository;
  private final BookingMapper bookingMapper;
  private final CreatePdfService pdfService;
  private final PerformanceRepository performanceRepository;
  private final SeatRepository seatRepository;
  private final TicketRepository ticketRepository;
  private final BookingRepository bookingRepository;
  private final TicketValidationService validationService;

  private final SeatMapper seatMapper;

  private static Booking getLatestBooking(ApplicationUser user) {
    List<Booking> bookings = user.getBookings();

    LocalDateTime latestDate = bookings.stream()
      .map(Booking::getCreatedDate)
      .toList().stream().max(Comparator.naturalOrder())
      .orElseThrow(NotFoundException::new);

    Optional<Booking> latestBooking = bookings.stream()
      .filter(booking -> booking.getCreatedDate().equals(latestDate))
      .findFirst();

    return latestBooking.orElseThrow(NotFoundException::new);
  }

  private static String getEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
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

  @Override
  @Transactional
  public BookingDetailDto makeBooking(Collection<TicketBookingDto> tickets, BookingType type) {
    ensureSeatsAreAvailable(tickets);

    Booking booking = createBookingFromTickets(tickets, type);
    ApplicationUser user = attachBookingToUser(booking);

    return bookingMapper.map(booking, getEmail());
  }

  private ApplicationUser attachBookingToUser(Booking booking) {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    user.addBooking(booking);
    booking.setUser(user);
    bookingRepository.save(booking);
    return userRepository.save(user);
  }

  private Booking createBookingFromTickets(Collection<TicketBookingDto> tickets, BookingType type) {
    Booking booking = new Booking();
    booking.setBookingType(type);
    booking.setCreatedDate(LocalDateTime.now());

    bookingRepository.save(booking);

    List<Ticket> ticketList = tickets
      .stream()
      .map(ticketDto -> Ticket.builder()
        .performance(performanceRepository.findById(ticketDto.getPerformanceId())
          .orElseThrow(() -> new NotFoundException("Performance with id " + ticketDto.getPerformanceId() + " not found")))
        .seat(seatRepository.findById(ticketDto.getSeat().getId())
          .orElseThrow(() -> new NotFoundException("Seat with id " + ticketDto.getPerformanceId() + " not found")))
        .booking(booking)
        .price(seatRepository.findById(ticketDto.getSeat().getId())
          .orElseThrow(() -> new NotFoundException("Seat with id " + ticketDto.getSeat().getId() + " could not be found"))
          .getSector().getPriceCategory().getPricingList()
          .stream()
          .filter(pc -> pc.getPerformance().getId().equals(ticketDto.getPerformanceId()))
          .findFirst()
          .orElseThrow(() ->
            new NotFoundException("No pricing information for performance with id " + ticketDto.getPerformanceId()))
          .getPricing())
        .build())
      .toList();

    ticketRepository.saveAll(ticketList);

    ticketList.forEach((ticket) -> {
      ticket.setValidationHash(validationService.generateTicketValidationHash(ticket));
      booking.addTicket(ticket);
    });

    return booking;
  }

  private void ensureSeatsAreAvailable(Collection<TicketBookingDto> tickets) {
    tickets.forEach((t) -> {
      Seat seat = seatRepository.findById(t.getSeat().getId())
        .orElseThrow(() -> new NotFoundException("Seat with id " + t.getSeat().getId() + " could not be found"));
      SeatDto.State seatState = seatMapper.seatToSeatDtoForPerformance(seat, t.getPerformanceId()).getState();
      /*
      Needs to be commented out because SeatState is not sensible yet
      if (seatState != SeatDto.State.FREE) {
        throw new SeatNotAvailableException(seat, seatState);
      }
      */
    });

    if (false) {
      throw new RuntimeException();
    }
  }


  @SneakyThrows
  public byte[] createPdfForBooking(Booking savedBooking, String domain) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();

    pdfService.createTicketPdf(stream, savedBooking.getTickets(), domain);
    return stream.toByteArray();
  }

  @Override
  public Booking getById(long id) throws NotFoundException {
    return bookingRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Booking with the id " + id + " could not be found"));
  }

}

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
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.DocumentType;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.SeatNotAvailableException;
import at.ac.tuwien.sepm.groupphase.backend.exception.TicketCancellationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.CreatePdfService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketValidationService;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType.CANCELLATION;
import static at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType.PURCHASE;
import static at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType.RESERVATION;

/**
 * Implements the Booking Service Interface used by the customer to book tickets.
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
  private final UserRepository userRepository;
  private final BookingMapper bookingMapper;
  private final CreatePdfService pdfService;
  private final PerformanceRepository performanceRepository;
  private final SeatRepository seatRepository;
  private final TicketRepository ticketRepository;
  private final BookingRepository bookingRepository;
  private final TicketValidationService validationService;

  private final SeatMapper seatMapper;

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

    booking.calculateTotal();
    return bookingMapper.map(booking, getEmail());
  }

  @Override
  public List<BookingItemDto> fetchBookings() {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());

    return bookingMapper.map(bookingRepository.getBookingByUserId(user.getId()));
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

    List<Ticket> expired = booking.getTickets()
      .stream()
      .filter((t) -> t.getPerformance().getStartDate().isBefore(LocalDateTime.now())).toList();
    if (expired.size() != 0) {
      throw new TicketCancellationException("Tickets %s cannot be cancelled because Performance has already started"
        .formatted(expired
          .stream()
          .map(t -> t.getId().toString())
          .reduce("", (whole, single) -> whole + ", #" + single)));
    }

    List<Ticket> used = booking.getTickets()
      .stream()
      .filter(Ticket::isUsed)
      .toList();

    if (used.size() != 0) {
      throw new TicketCancellationException("Tickets %s cannot be cancelled because it was already used"
        .formatted(used
          .stream()
          .map(t -> t.getId().toString())
          .reduce("", (whole, single) -> whole + ", #" + single)));
    }

    if (booking.getBookingType() == PURCHASE) {
      booking.cancel();
    } else {
      booking.revoke();
    }

    bookingRepository.save(booking);
  }

  @Override
  @Transactional
  public void purchaseReservation(Long bookingId) {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    Booking booking = getBookingFromCurrentUser(bookingId);

    booking.setBookingType(PURCHASE);

    userRepository.save(user);
  }

  private Booking getBookingFromCurrentUser(Long bookingId) {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    Optional<Booking> matchingBooking = user.getBookings()
      .stream()
      .filter(booking -> Objects.equals(booking.getId(), bookingId))
      .findFirst();

    return matchingBooking.orElseThrow(() ->
      new NotFoundException("No booking with id " + bookingId + " found for current user")
    );
  }

  @Override
  @Transactional
  public BookingDetailDto makeBooking(Collection<TicketBookingDto> tickets, BookingType type) {
    ensureSeatsAreAvailable(tickets);

    Booking booking = createBookingFromTickets(tickets, type);
    attachBookingToUser(booking);

    return bookingMapper.map(booking, getEmail());
  }

  private void attachBookingToUser(Booking booking) {
    ApplicationUser user = userRepository.findUserByEmail(getEmail());
    user.addBooking(booking);
    booking.setUser(user);
    bookingRepository.save(booking);
    userRepository.save(user);
  }

  private Booking createBookingFromTickets(Collection<TicketBookingDto> tickets, BookingType type) {
    Booking booking = new Booking();
    booking.setBookingType(type);
    booking.setCreatedDate(LocalDateTime.now());

    bookingRepository.save(booking);

    List<Ticket> ticketList = tickets
      .stream()
      .map(ticketDto -> ticketRepository.findBySeatAndPerformanceId(ticketDto.getSeat().getId(), ticketDto.getPerformanceId()).toBuilder().booking(booking).build())
      .toList();

    ticketList.forEach((ticket) -> {
      ticket.setValidationHash(validationService.generateTicketValidationHash(ticket));
      booking.addTicket(ticket);
    });

    ticketRepository.saveAll(ticketList);
    return booking;
  }

  private void ensureSeatsAreAvailable(Collection<TicketBookingDto> tickets) {
    tickets.forEach((t) -> {
      Seat seat = seatRepository.findById(t.getSeat().getId())
        .orElseThrow(() -> new NotFoundException("Seat with id " + t.getSeat().getId() + " could not be found"));
      SeatDto.State seatState = seatMapper.seatToSeatDtoForPerformance(seat, t.getPerformanceId()).getState();

      if (seatState != SeatDto.State.FREE) {
        throw new SeatNotAvailableException(seat, seatState);
      }

    });
  }

  @Override
  public byte[] createPdfForBooking(Long bookingId, String domain, DocumentType type) throws DocumentException, IOException, WriterException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    Booking booking = getBookingFromCurrentUser(bookingId);

    switch (type) {
      case RECEIPT -> {
        if (booking.getBookingType() == RESERVATION) {
          throw new IllegalStateException("Cannot provide receipt for reservations");
        }
        pdfService.createReceiptPdf(stream, booking, domain);
      }
      case TICKETS -> {
        if (booking.getBookingType() != PURCHASE) {
          throw new IllegalStateException("Cannot generate tickets for reserved or canceled bookings");
        }
        pdfService.createTicketPdf(stream, booking, domain);
      }
      case CANCELLATION -> {
        if (booking.getBookingType() != CANCELLATION) {
          throw new IllegalStateException("Cannot generate cancellation for valid bookings");
        }
        pdfService.createCancellationPdf(stream, booking, domain);
      }
      default -> throw new IllegalStateException("Unexpected value: " + type);
    }

    return stream.toByteArray();
  }


  @Override
  public Booking getById(long id) throws NotFoundException {
    return bookingRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Booking with the id " + id + " could not be found"));
  }

}

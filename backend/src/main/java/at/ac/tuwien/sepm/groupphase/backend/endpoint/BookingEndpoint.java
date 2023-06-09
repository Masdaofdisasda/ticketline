package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingFullDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketBookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketFullDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.DocumentType;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/booking")
public class BookingEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final BookingService bookingService;

  /**
   * Make reservations for multiple tickets and create a new booking for a user.
   *
   * @param tickets to reserve
   * @return Booking number
   */
  @PostMapping(value = "/reservations")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public BookingDetailDto reserveTickets(@RequestBody List<TicketBookingDto> tickets) throws ValidationException {
    LOGGER.info("POST /api/v1/booking/reservations: reserveTickets({})", tickets);

    return bookingService.makeBooking(tickets, BookingType.RESERVATION);
  }

  @GetMapping("/{bookingId}/ticketsPdf")
  @Secured("ROLE_USER")
  @Transactional(readOnly = true)
  public ResponseEntity<byte[]> generateTicketsPdf(@PathVariable long bookingId, @RequestHeader("referer") String senderUri)
      throws DocumentException, IOException, WriterException {
    LOGGER.info("GET /api/v1/booking/{}/ticketsPdf: generateTicketsPdf({})", bookingId, bookingId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .header("Content-Type", MediaType.APPLICATION_PDF_VALUE)
        .body(bookingService.createPdfForBooking(bookingId,
            senderUri, DocumentType.TICKETS));
  }

  @GetMapping("/{bookingId}/receiptPdf")
  @Secured("ROLE_USER")
  @Transactional(readOnly = true)
  public ResponseEntity<byte[]> generateReceiptPdf(@PathVariable long bookingId, @RequestHeader("referer") String senderUri)
      throws DocumentException, IOException, WriterException {
    LOGGER.info("GET /api/v1/booking/{}/receiptPdf: generateReceiptPdf({})", bookingId, bookingId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .header("Content-Type", MediaType.APPLICATION_PDF_VALUE)
        .body(bookingService.createPdfForBooking(bookingId,
            senderUri, DocumentType.RECEIPT));
  }

  @GetMapping("/{bookingId}/cancellationPdf")
  @Secured("ROLE_USER")
  @Transactional(readOnly = true)
  public ResponseEntity<byte[]> generateCancellationPdf(@PathVariable long bookingId, @RequestHeader("referer") String senderUri)
      throws DocumentException, IOException, WriterException {
    LOGGER.info("GET /api/v1/booking/{}/cancellationPdf: generateCancellationPdf({})", bookingId, bookingId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .header("Content-Type", MediaType.APPLICATION_PDF_VALUE)
        .body(bookingService.createPdfForBooking(bookingId,
            senderUri, DocumentType.CANCELLATION));
  }

  /**
   * purchase multiple tickets and create a new booking for a user.
   *
   * @param tickets to purchase
   * @return Booking number
   */
  @PostMapping(value = "/purchases")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public BookingDetailDto purchaseTickets(@RequestBody List<TicketBookingDto> tickets) throws ValidationException {
    LOGGER.info("POST /api/v1/booking/purchases: makeBooking({})", tickets);
    return bookingService.makeBooking(tickets, BookingType.PURCHASE);
  }

  /**
   * fetches booking information for a single booking. Can be called after a booking was completed
   *
   * @param bookingId of the finished booking
   * @return details about the booking
   */
  @GetMapping("/{bookingId}")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  @Transactional
  public BookingDetailDto fetchBooking(@PathVariable Long bookingId) {
    LOGGER.info("GET /api/v1/booking/{}: fetchBooking({})", bookingId, bookingId);
    return bookingService.fetchBookingDetails(bookingId);
  }

  /**
   * fetches booking information for a single booking. Used to select tickets of a booking
   *
   * @param bookingId of the finished booking
   * @return details about the booking
   */
  @GetMapping("/tickets/{bookingId}")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  @Transactional
  public BookingFullDto fetchFullBooking(@PathVariable Long bookingId) {
    LOGGER.info("GET /api/v1/booking/tickets/{}: fetchFullBooking({})", bookingId, bookingId);
    return bookingService.fetchBookingFull(bookingId);
  }

  /**
   * fetches all bookings of the current user.
   *
   * @return list of bookings
   */
  @GetMapping
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  @Transactional
  public Stream<BookingItemDto> fetchBookings() {
    LOGGER.info("GET /api/v1/booking/: fetchBookings()");
    return bookingService.fetchBookings().stream().sorted(Comparator.comparing(BookingItemDto::getBookedOn).reversed());
  }

  /**
   * Cancels a single booking and removes it forever.
   *
   * @param bookingId of the booking to cancel
   */
  @DeleteMapping("/{bookingId}")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public void cancelBooking(@PathVariable Long bookingId) {
    LOGGER.info("DELETE /api/v1/booking/{}: cancelBooking({})", bookingId, bookingId);
    bookingService.cancelBooking(bookingId);
  }

  /**
   * Updates a booking to be of type "purchased".
   *
   * @param bookingId of the booking to update
   */
  @PostMapping(value = "/{bookingId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public void purchaseReservation(@PathVariable Long bookingId, @RequestBody List<TicketFullDto> tickets) throws ValidationException {
    LOGGER.info("PUT /api/v1/booking/{}: purchaseReservation({},{})", bookingId, bookingId, tickets);
    bookingService.purchaseReservation(bookingId, tickets);
  }

}

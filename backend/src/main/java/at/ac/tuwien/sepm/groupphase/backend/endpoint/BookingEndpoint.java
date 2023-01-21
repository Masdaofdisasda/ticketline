package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketBookingDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import at.ac.tuwien.sepm.groupphase.backend.service.CreatePdfService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/booking")
@RequiredArgsConstructor
public class BookingEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final BookingService bookingService;
  private final CreatePdfService pdfService;

  /**
   * Make reservations for multiple tickets and create a new booking for a user.
   *
   * @param tickets to reserve
   * @return Booking number
   */
  @PostMapping(value = "/reservations")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public BookingDetailDto reserveTickets(@RequestBody List<TicketBookingDto> tickets) {
    LOGGER.info("POST /api/v1/booking/reservations: reserveTickets({})", tickets);

    return bookingService.makeBooking(tickets, BookingType.RESERVATION);
  }

  @GetMapping("/{id}/pdf")
  @PermitAll
  @Transactional(readOnly = true)
  public ResponseEntity<byte[]> generatePdf(HttpServletRequest request, @PathVariable long id) {
    LOGGER.info("POST /api/v1/booking/{}/pdf: generatePdf({})", id, id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .header("Content-Type", MediaType.APPLICATION_PDF_VALUE)
      .body(bookingService.createPdfForBooking(bookingService.getById(id),
        request.getServerName() + ":" + request.getServerPort()));
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
  public BookingDetailDto purchaseTickets(@RequestBody List<TicketBookingDto> tickets) {
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
  public BookingDetailDto fetchBooking(@PathVariable Long bookingId) {
    LOGGER.info("GET /api/v1/booking/{}: fetchBooking({})", bookingId, bookingId);
    return bookingService.fetchBookingDetails(bookingId);
  }

  /**
   * fetches all bookings of the current user.
   *
   * @return list of bookings
   */
  @GetMapping
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public List<BookingItemDto> fetchBookings() {
    LOGGER.info("GET /api/v1/booking/: fetchBookings()");
    return bookingService.fetchBookings();
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
  @PutMapping("/{bookingId}")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public void purchaseReservation(@PathVariable Long bookingId) {
    LOGGER.info("PUT /api/v1/booking/{}: purchaseReservation({})", bookingId, bookingId);
    bookingService.purchaseReservation(bookingId);
  }

}

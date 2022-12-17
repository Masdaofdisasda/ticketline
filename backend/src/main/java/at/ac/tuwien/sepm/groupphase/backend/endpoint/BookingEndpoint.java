package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/booking")
@RequiredArgsConstructor
public class BookingEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final BookingService bookingService;

  /**
   * Make reservations for multiple tickets.
   *
   * @param tickets to reserve
   * @return Booking id
   */
  @PostMapping(value = "/reservations")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public Long reserveTickets(@RequestBody List<TicketDto> tickets) {
    LOGGER.info("POST /api/v1/booking/reservations: reserveTickets({})", tickets);
    return bookingService.reserveTickets(tickets);
  }

  /**
   * purchase multiple tickets.
   *
   * @param tickets to purchase
   * @return Booking id
   */
  @PostMapping(value = "/purchases")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public Long purchaseTickets(@RequestBody List<TicketDto> tickets) {
    LOGGER.info("POST /api/v1/booking/purchases: purchaseTickets({})", tickets);
    return bookingService.purchaseTickets(tickets);
  }

  @GetMapping("/{bookingId}")
  @Secured("ROLE_USER")
  @Operation(security = @SecurityRequirement(name = "apiKey"))
  public BookingDetailDto fetchBooking(@PathVariable Long bookingId) {
    LOGGER.info("GET /api/v1/booking/{}}: fetchBooking({})", bookingId, bookingId);
    return bookingService.fetchBookingDetails(bookingId);
  }
}

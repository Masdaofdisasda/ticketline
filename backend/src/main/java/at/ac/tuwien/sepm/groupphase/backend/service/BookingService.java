package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;

import java.util.Collection;

/**
 * Service for the ticket booking process.
 */
public interface BookingService {

  /**
   * Reserves tickets for the given seats if they are still available.
   *
   * @param tickets for the seats the user wants to reserve
   * @return the reservation number needed to collect the reserved tickets
   */
  Long reserveTickets(Collection<TicketDto> tickets);

  /**
   * Purchases tickets for the given seats if they are still available.
   *
   * @param tickets for the seats the user wants to purchase
   * @return Order id for the purchase
   */
  Long purchaseTickets(Collection<TicketDto> tickets);

  /**
   * Fetches information about a completed order.
   *
   * @param bookingId of the order to fetch
   * @return the details of the requested order
   */
  BookingDetailDto fetchBookingDetails(Long bookingId);

}

package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketBookingDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.DocumentType;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Service for the ticket booking process.
 */
public interface BookingService {

  /**
   * Purchases tickets for the given seats if they are still available.
   *
   * @param tickets for the seats the user wants to purchase
   * @return Order id for the purchase
   */
  BookingDetailDto makeBooking(Collection<TicketBookingDto> tickets, BookingType type);

  /**
   * Fetches information about a completed booking. Only bookings made by the user can be fetched
   *
   * @param bookingId of the order to fetch
   * @return the details of the requested order
   */
  BookingDetailDto fetchBookingDetails(Long bookingId);

  /**
   * Fetches all bookings made by the current user.
   *
   * @return List of bookings
   */
  List<BookingItemDto> fetchBookings();

  /**
   * Sets purchase made by the user to the status canceled, and reservations will be deleted.
   *
   * @param bookingId to cancel
   */
  void cancelBooking(Long bookingId);

  /**
   * Sets booking made by the user to the status purchased.
   *
   * @param bookingId to purchase
   */
  void purchaseReservation(Long bookingId);

  /**
   * Creates a PDF of given type for a booking.
   *
   * @param bookingId The booking the PDF should be created for
   * @param domain    of the server
   * @param type      of the PDF to create
   * @return The bytes that make up the PDF file
   */
  byte[] createPdfForBooking(Long bookingId, String domain, DocumentType type) throws DocumentException, IOException, WriterException;

  /**
   * Gets a single Booking by its id.
   *
   * @param id The id of the booking
   * @return The booking with the given id
   */
  Booking getById(long id);
}

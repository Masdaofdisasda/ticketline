package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;

public interface TicketValidationService {
  /**
   * This method creates a Hash that can be used to validate the ticket @see {@link TicketValidationService#validateTicket(byte[])}).
   *
   * @param ticket The ticket that the hash should be created for
   * @return The generated hash for validating a ticket
   */
  byte[] generateTicketValidationHash(Ticket ticket);

  /**
   * This method takes a validation hash that was created with {@link TicketValidationService#generateTicketValidationHash(Ticket)}
   * and returns a appropriate {@link TicketValidationResult}.
   *
   * @param validationHash The hash that is used to validate
   * @return {@link TicketValidationResult#DECRYPTION_FAILED}: is returned when an error in the decryption arises (e.g. when qr code data was read wrong)
   *      {@link TicketValidationResult#TICKET_NOT_FOUND}: is returned when the hash was decrypted but the associated ticket does not exist in persistence
   *      {@link TicketValidationResult#TICKET_ALREADY_USED}: is returned when the ticket was found, but the data says it was already redeemed
   *      {@link TicketValidationResult#VALID}: is returned when everything went right and can be used
   */
  TicketValidationResult validateTicket(byte[] validationHash) throws InterruptedException;

  enum TicketValidationResult {
    BOOKING_CANCELED(""),
    NO_BOOKING_FOUND(""),
    DECRYPTION_FAILED(""),
    TICKET_NOT_FOUND(""),
    TICKET_ALREADY_USED(""),
    VALID("");

    TicketValidationResult(String message) {
    }
  }
}

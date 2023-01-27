package at.ac.tuwien.sepm.groupphase.backend.exception;

public class TicketCancellationException extends RuntimeException {
  public TicketCancellationException(String message) {
    super(message);
  }
}

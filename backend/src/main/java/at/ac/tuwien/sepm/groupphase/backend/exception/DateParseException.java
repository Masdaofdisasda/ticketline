package at.ac.tuwien.sepm.groupphase.backend.exception;

public class DateParseException extends RuntimeException {

  public DateParseException() {
  }

  public DateParseException(String message) {
    super(message);
  }

  public DateParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public DateParseException(Exception e) {
    super(e);
  }
}

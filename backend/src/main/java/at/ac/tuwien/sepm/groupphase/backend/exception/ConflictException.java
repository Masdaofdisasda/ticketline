package at.ac.tuwien.sepm.groupphase.backend.exception;

public class ConflictException extends RuntimeException {

  public ConflictException() {
  }

  public ConflictException(String message) {
    super(message);
  }
}

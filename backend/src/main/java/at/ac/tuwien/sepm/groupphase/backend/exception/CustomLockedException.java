package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.security.authentication.LockedException;

public class CustomLockedException extends LockedException {

  public CustomLockedException(String msg) {
    super(msg);
  }

  public CustomLockedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}

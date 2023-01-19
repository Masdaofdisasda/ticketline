package at.ac.tuwien.sepm.groupphase.backend.exception;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;

public class SeatNotAvailableException extends IllegalStateException {

  public SeatNotAvailableException(Seat seat, SeatDto.State state) {
    super("Seat " + seat.getId() + " is not available, state is currently, " + state.toString());
  }
}

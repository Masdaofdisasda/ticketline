package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class VenueValidator {

  public void validateVenue(final Venue venue) throws ValidationException {
    List<String> errors = new ArrayList<>();

    for (final Room room : venue.getRooms()) {
      if (room.getColumnSize() < 1) {
        errors.add("The column size of room " + room.getName() + " must be greater than 0, is" + room.getColumnSize());
      }
      if (room.getRowSize() < 1) {
        errors.add("The row size of room " + room.getName() + " must be greater than 0, is" + room.getRowSize());
      }
      for (final Sector sector : room.getSectors()) {
        for (final Seat seat : sector.getSeats()) {
          if (seat.getColNumber() >= room.getRowSize()) {
            errors.add("Seats column number cannot be greater than the rooms row size");
          }
          if (seat.getRowNumber() >= room.getColumnSize()) {
            errors.add("Seats row number cannot be greater than the rooms column size");
          }
        }
      }
    }

    if (!errors.isEmpty()) {
      throw new ValidationException("Validation of venue failed", errors);
    }
  }
}

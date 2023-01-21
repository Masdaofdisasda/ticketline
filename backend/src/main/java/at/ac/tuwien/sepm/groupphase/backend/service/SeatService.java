package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

public interface SeatService {

  /**
   * Adds a {@link Seat} to the persistent storage.
   *
   * @param toAdd the information about the seat that should be added
   * @return the seat that was added including its ID
   */
  Seat createSeat(Seat toAdd);

  /**
   * Get the seat with the given ID.
   *
   * @param id The ID of the seat
   * @return The seat with the given ID
   * @throws NotFoundException if no seat with the ID is found in persistent storage
   */
  Seat getById(long id) throws NotFoundException;

  /**
   * Edit a seat with the given ID.
   *
   * @param id The id of the seat that should be edited
   * @param edit The data that the seat is updated with
   * @return The edited {@link Seat}
   */
  Seat editSeat(long id, SeatEditDto edit);
}

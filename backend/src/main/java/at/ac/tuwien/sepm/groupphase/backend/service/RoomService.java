package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

public interface RoomService {
  /**
   * Delete a {@link Room} from the persistent storage.
   *
   * @param id The ID of the room that should be deleted
   * @return The room that was deleted
   */
  Room deleteRoom(long id) throws NotFoundException;

  /**
   * Edit a room with the given data.
   *
   * @param id The id of the room to edit
   * @param toEdit the data of the edit
   * @return the edited room
   * @throws NotFoundException if no room with the given id was found
   */
  Room editRoom(long id, Room toEdit) throws NotFoundException;

  /**
   * Gets a single room by its id.
   *
   * @return The room with the given id
   */
  Room getById(long id) throws NotFoundException;
}

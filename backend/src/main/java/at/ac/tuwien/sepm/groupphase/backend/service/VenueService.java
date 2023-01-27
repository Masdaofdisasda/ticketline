package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import java.util.List;

public interface VenueService {
  /**
   * adds a {@link Venue} to the persistent storage.
   *
   * @return The {@link Venue} that was added
   */
  Venue addVenue(Venue toAdd) throws ValidationException;

  /**
   * Remove a record of a {@link Venue} from the persistent storage.
   *
   * @param id The ID of the {@link Venue} that should be deleted
   */
  void deleteVenue(long id);

  /**
   * Add a room to the list of {@link at.ac.tuwien.sepm.groupphase.backend.entity.Room Rooms} of a {@link Venue}.
   *
   * @param id The ID of the venue that the room should be added to
   * @param roomId The ID of the {@link at.ac.tuwien.sepm.groupphase.backend.entity.Room Room} that should be added to the {@link Venue}
   * @return The updated {@link Venue}
   */
  Venue addRoomToLocation(long id, long roomId);

  /**
   * Edit a venue.
   *
   * @param id The id of the venue to edit
   * @param edit The data to edit the venue with
   * @return The edited venue
   * @throws NotFoundException if no venue with the given id can be found
   */
  Venue editVenue(long id, VenueEditDto edit) throws NotFoundException, ValidationException;

  /**
   * Get all saved locations.
   *
   * @return a list of all venue in persistence
   */
  List<Venue> getAllVenues();

  /**
   * Get a venue by its id.
   *
   * @param id The id of the venue
   * @return The venue with the given id
   */
  Venue getById(long id) throws NotFoundException;
}

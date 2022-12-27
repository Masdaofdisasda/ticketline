package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

public interface SectorService {

  /**
   * Create a new sector.
   *
   * @param toAdd The data of the new sector
   * @return The sector that was added
   */
  Sector createSector(Sector toAdd);

  /**
   * Add an existing seat to a sector.
   *
   * @param sectorId The id of the sector the seat should be added to
   * @param seatId The id of the seat that should be added
   * @return The sector after the transaction
   * @throws NotFoundException if either no sector with ID=sectorId or no seat with ID=seatId was found
   */
  Sector addSeatToSector(long sectorId, long seatId) throws NotFoundException;

  /**
   * Removes a seat from a sector.
   *
   * @param sectorId The ID of the sector the seat should be removed from
   * @param seatId The ID of the seat that should be removed
   * @return The sector after the transaction
   * @throws NotFoundException if either no sector with ID=sectorId or no seat with ID=seatId was found
   */
  Sector removeSeatFromSector(long sectorId, long seatId) throws NotFoundException;

  /**
   * Edit a sector.
   *
   * @param id The id of the sector to edit
   * @param edit The data of the edited sector
   * @return The edited sector
   */
  Sector editSector(long id, SectorEditDto edit);
}

package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatService;
import at.ac.tuwien.sepm.groupphase.backend.service.SectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SectorServiceImpl implements SectorService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final SectorRepository repository;
  private final SeatService seatService;

  public SectorServiceImpl(SectorRepository repository, SeatService seatService) {
    this.repository = repository;
    this.seatService = seatService;
  }

  @Override
  public Sector createSector(Sector toAdd) {
    return repository.save(toAdd);
  }

  @Override
  public Sector addSeatToSector(long sectorId, long seatId) throws NotFoundException  {
    Sector sector = repository.getReferenceById(sectorId);
    sector.getSeats().add(seatService.getById(seatId));
    repository.save(sector);
    return sector;
  }

  @Override
  public Sector removeSeatFromSector(long sectorId, long seatId) throws NotFoundException {
    Sector sector = repository.getReferenceById(sectorId);
    sector.getSeats().remove(seatService.getById(seatId));
    repository.save(sector);
    return sector;
  }

  @Override
  public Sector editSector(long id, SectorEditDto edit) throws NotFoundException {
    Sector sector = repository.getReferenceById(id);
    if (edit.getName() != null) {
      sector.setName(edit.getName());
    }
    if (edit.getPriceCategory() != null) {
      sector.setPriceCategory(edit.getPriceCategory());
    }
    repository.save(sector);
    return sector;
  }


}

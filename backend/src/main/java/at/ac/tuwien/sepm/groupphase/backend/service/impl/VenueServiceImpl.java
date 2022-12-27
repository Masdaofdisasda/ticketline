package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final VenueRepository repository;
  private final RoomRepository roomRepository;
  private final SectorRepository sectorRepository;
  private final SeatRepository seatRepository;

  private final VenueValidator venueValidator;

  private final PriceCategoryRepository priceCategoryRepository;

  public VenueServiceImpl(VenueRepository repository,
                          RoomRepository roomRepository,
                          SectorRepository sectorRepository,
                          SeatRepository seatRepository,
                          VenueValidator venueValidator, PriceCategoryRepository priceCategoryRepository) {
    this.repository = repository;
    this.roomRepository = roomRepository;
    this.sectorRepository = sectorRepository;
    this.seatRepository = seatRepository;
    this.venueValidator = venueValidator;
    this.priceCategoryRepository = priceCategoryRepository;
  }

  @Override
  public Venue addVenue(Venue toAdd) throws ValidationException {
    LOGGER.trace("addVenue({})", toAdd);

    for (Room room : toAdd.getRooms()) {
      for (Sector sector : room.getSectors()) {
        for (Seat seat : sector.getSeats()) {
          seat.setSector(sector);
        }
        sector.setPriceCategory(priceCategoryRepository.findById(sector.getPriceCategory().getId()).orElseThrow());
        sector.setRoom(room);
      }
      room.setVenue(toAdd);
    }
    venueValidator.validateVenue(toAdd);

    return repository.save(toAdd);
  }

  @Override
  public Venue deleteVenue(long id) {
    LOGGER.trace("deleteVenue({})", id);
    final Venue venue = repository.getReferenceById(id);
    repository.deleteById(id);
    return venue;
  }

  @Override
  public Venue addRoomToLocation(long id, long roomId) {
    LOGGER.trace("addRoomToLocation({}, {})", id, roomId);
    Room room = roomRepository.getReferenceById(roomId);
    Venue venue = repository.getReferenceById(id);
    room.setVenue(venue);
    venue.getRooms().add(room);
    repository.save(venue);
    return venue;
  }

  @Override
  public Venue editVenue(long id, VenueEditDto edit) throws NotFoundException, ValidationException {
    LOGGER.trace("editVenue({})", id);
    Venue venue = repository.getReferenceById(id);
    if (edit.getName() != null) {
      venue.setName(edit.getName());
    }
    if (edit.getStreet() != null) {
      venue.setStreet(edit.getStreet());
    }
    if (edit.getHouseNumber() != null) {
      venue.setHouseNumber(edit.getHouseNumber());
    }
    if (edit.getCity() != null) {
      venue.setCity(edit.getCity());
    }
    if (edit.getZipCode() != null) {
      venue.setZipCode(edit.getZipCode());
    }
    if (edit.getCountry() != null) {
      venue.setCountry(edit.getCountry());
    }

    venueValidator.validateVenue(venue);

    repository.save(venue);
    return venue;
  }

  @Override
  public List<Venue> getAllVenues() {
    LOGGER.trace("getAllVenues()");
    return repository.findAll();
  }

  @Override
  public Venue getById(long id) throws NotFoundException {
    LOGGER.trace("getLocationByID({})", id);
    Optional<Venue> venueOptional = repository.findById(id);
    if (venueOptional.isEmpty()) {
      throw new NotFoundException("Venue with id " + id + " could not be found");
    }
    return venueOptional.get();
  }

}

package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoomMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectorMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
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
import at.ac.tuwien.sepm.groupphase.backend.service.validator.VenueValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
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

  private final VenueMapper venueMapper;
  private final RoomMapper roomMapper;
  private final SectorMapper sectorMapper;
  private final SeatMapper seatMapper;

  public VenueServiceImpl(VenueRepository repository,
                          RoomRepository roomRepository,
                          SectorRepository sectorRepository,
                          SeatRepository seatRepository,
                          VenueValidator venueValidator, PriceCategoryRepository priceCategoryRepository, VenueMapper venueMapper, RoomMapper roomMapper, SectorMapper sectorMapper, SeatMapper seatMapper) {
    this.repository = repository;
    this.roomRepository = roomRepository;
    this.sectorRepository = sectorRepository;
    this.seatRepository = seatRepository;
    this.venueValidator = venueValidator;
    this.priceCategoryRepository = priceCategoryRepository;
    this.venueMapper = venueMapper;
    this.roomMapper = roomMapper;
    this.sectorMapper = sectorMapper;
    this.seatMapper = seatMapper;
  }

  @Override
  public Venue addVenue(Venue toAdd) throws ValidationException {
    LOGGER.trace("addVenue({})", toAdd);

    venueValidator.validateVenue(toAdd);

    for (Room room : toAdd.getRooms()) {
      for (Sector sector : room.getSectors()) {
        for (Seat seat : sector.getSeats()) {
          seat.setSector(sector);
        }
        sector.setPriceCategory(priceCategoryRepository.findById(sector.getPriceCategory().getId()).orElse(sector.getPriceCategory()));
        sector.setRoom(room);
      }
      room.setVenue(toAdd);
    }

    return repository.save(toAdd);
  }

  @Override
  public Venue deleteVenue(long id) {
    LOGGER.trace("deleteVenue({})", id);
    final Optional<Venue> venueOptional = repository.findById(id);

    if (venueOptional.isEmpty()) {
      throw new NotFoundException("Venue with id " + id + " could not be found");
    }

    repository.deleteById(id);
    return venueOptional.get();
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
    Optional<Venue> venueOptional = repository.findById(id);

    if (venueOptional.isEmpty()) {
      throw new NotFoundException("Venue with id " + id + " could not be found");
    }

    Venue venue = venueOptional.get();

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

    if (edit.getRooms() != null) {
      venue.getRooms().forEach(room -> {
        room.setVenue(null);
        roomRepository.save(room);
      });
      venue.setRooms(new ArrayList<>());
      for (RoomEditDto room : edit.getRooms()) {
        Room editRoom = editRoom(room);
        editRoom.setVenue(venue);
        venue.getRooms().add(editRoom);
      }
    }

    venueValidator.validateVenue(venue);

    return repository.save(venue);
  }

  private Room editRoom(RoomEditDto room) {
    Room editRoom = room.getId() == null ? roomMapper.roomEditDtoToRoom(room) :
      roomRepository.findById(room.getId()).orElseThrow(() -> new NotFoundException("The room with the id " + room.getId() + " does not exist"));
    if (room.getId() != null) {
      // room already exists
      if (room.getName() != null) {
        editRoom.setName(room.getName());
      }
      if (room.getRowSize() != null) {
        editRoom.setRowSize(room.getRowSize());
      }
      if (room.getColumnSize() != null) {
        editRoom.setColumnSize(room.getColumnSize());
      }

      if (room.getSectors() != null) {
        // reset current rooms in sector
        editRoom.getSectors().forEach(sector -> {
          sector.setRoom(null);
          sectorRepository.save(sector);
        });
        editRoom.setSectors(new ArrayList<>());
        for (SectorEditDto sector : room.getSectors()) {
          Sector editSector = editSector(sector);
          editSector.setRoom(editRoom);
          editRoom.getSectors().add(editSector);
        }
      }
    } else {
      if (editRoom.getSectors() != null) {
        for (Sector sector : editRoom.getSectors()) {
          sector.setRoom(editRoom);
          if (sector.getSeats() != null) {
            for (Seat seat : sector.getSeats()) {
              seat.setSector(sector);
            }
          }
        }
      }
    }

    return editRoom;
  }

  private Sector editSector(SectorEditDto sector) {
    if (sector.getId() == null) {
      // return here because adding the sector was handled by room mapping
      return sectorMapper.sectorEditDtoToSector(sector);
    }
    Sector editSector = sectorRepository.findById(sector.getId())
      .orElseThrow(() -> new NotFoundException("The sector with the id " + sector.getId() + " does not exist"));
    if (sector.getName() != null) {
      editSector.setName(sector.getName());
    }
    if (sector.getPriceCategory() != null) {
      editSector.setPriceCategory(priceCategoryRepository.findById(sector.getPriceCategory().getId())
        .orElseThrow(() -> new NotFoundException("The priceCategory with the id " + sector.getPriceCategory().getId() + " does not exist")));
    }

    if (sector.getSeats() != null) {
      editSector.getSeats().forEach(seat -> {
        seat.setSector(null);
        seatRepository.save(seat);
      });
      editSector.setSeats(new ArrayList<>());
      for (SeatEditDto seat : sector.getSeats()) {
        Seat editSeat = editSeat(seat);
        editSeat.setSector(editSector);
        editSector.getSeats().add(editSeat);
      }
    }

    return editSector;
  }

  private Seat editSeat(SeatEditDto seat) {
    if (seat.getId() == null) {
      return seatMapper.seatEditDtoToSeat(seat);
    }
    Seat editSeat = seatRepository.findById(seat.getId())
      .orElseThrow(() -> new NotFoundException("Seat with id " + seat.getId() + " does not exists"));

    // update values of editSeat
    editSeat.setRowNumber(ObjectUtils.firstNonNull(seat.getRowNumber(), editSeat.getRowNumber()));
    editSeat.setColNumber(ObjectUtils.firstNonNull(seat.getColNumber(), editSeat.getColNumber()));
    editSeat.setRowName(ObjectUtils.firstNonNull(seat.getRowName(), editSeat.getRowName()));
    editSeat.setColName(ObjectUtils.firstNonNull(seat.getColName(), editSeat.getColName()));

    return editSeat;
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

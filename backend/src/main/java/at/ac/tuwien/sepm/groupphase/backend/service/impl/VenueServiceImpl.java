package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoomMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectorMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import at.ac.tuwien.sepm.groupphase.backend.service.validator.VenueValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final VenueRepository venueRepository;
  private final RoomRepository roomRepository;
  private final SectorRepository sectorRepository;
  private final SeatRepository seatRepository;

  private final VenueValidator venueValidator;

  private final PriceCategoryRepository priceCategoryRepository;

  private final VenueMapper venueMapper;
  private final RoomMapper roomMapper;
  private final SectorMapper sectorMapper;
  private final SeatMapper seatMapper;
  private final SeatingPlanRepository seatingPlanRepository;

  public VenueServiceImpl(VenueRepository venueRepository,
                          RoomRepository roomRepository,
                          SectorRepository sectorRepository,
                          SeatRepository seatRepository,
                          VenueValidator venueValidator, PriceCategoryRepository priceCategoryRepository, VenueMapper venueMapper, RoomMapper roomMapper,
                          SectorMapper sectorMapper, SeatMapper seatMapper,
                          SeatingPlanRepository seatingPlanRepository) {
    this.venueRepository = venueRepository;
    this.roomRepository = roomRepository;
    this.sectorRepository = sectorRepository;
    this.seatRepository = seatRepository;
    this.venueValidator = venueValidator;
    this.priceCategoryRepository = priceCategoryRepository;
    this.venueMapper = venueMapper;
    this.roomMapper = roomMapper;
    this.sectorMapper = sectorMapper;
    this.seatMapper = seatMapper;
    this.seatingPlanRepository = seatingPlanRepository;
  }

  @Override
  public Venue addVenue(Venue toAdd) throws ValidationException {
    /*venueValidator.validateVenue(toAdd);

    for (Room room : toAdd.getRooms()) {
      Set<Sector> sectors = new HashSet<>();
      for (Sector sector : room.getSectors()) {
        sectorRepository.save(sector);
        for (Seat seat : sector.getSeats()) {
          seat.setSector(sector);
        }
        seatRepository.saveAll(sector.getSeats());
        sector.setPriceCategory(priceCategoryRepository.findById(sector.getPriceCategory().getId()).orElse(sector.getPriceCategory()));
        sector.setRoom(room);
        sectors.add(sector);
      }
      room.setSectors(sectors);
      room.setVenue(toAdd);
    }

    return venueRepository.save(toAdd);
*/

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

    return venueRepository.save(toAdd);
  }

  @Override
  public void deleteVenue(long id) {
    LOGGER.trace("deleteVenue({})", id);
    List<Room> roomsOfVenue = roomRepository.findAllByVenueWithPerformancesId(id);
    if (roomsOfVenue.isEmpty()) {
      throw new NotFoundException("Venue with id " + id + " could not be found.");
    }
    List<Performance> performancesOfVenue = roomsOfVenue.stream().flatMap(room -> new ArrayList<>(room.getPerformances()).stream()).toList();
    if (performancesOfVenue.isEmpty()) {
      venueRepository.deleteById(id);
    } else {
      throw new ConflictException("There are performances in this venue.");
    }
  }

  @Override
  public Venue addRoomToLocation(long id, long roomId) {
    LOGGER.trace("addRoomToLocation({}, {})", id, roomId);
    Room room = roomRepository.getReferenceById(roomId);
    Venue venue = venueRepository.getReferenceById(id);
    room.setVenue(venue);
    venue.getRooms().add(room);
    venueRepository.save(venue);
    return venue;
  }

  @Transactional
  @Override
  public Venue editVenue(long id, VenueEditDto edit) throws NotFoundException, ValidationException {
    LOGGER.trace("editVenue({})", id);
    Venue venue = venueRepository.findById(id).orElseThrow(() -> new NotFoundException("Venue with id " + id + " could not be found"));

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

    venue.getRooms().forEach(room -> {
      room.setVenue(null);
    });
    roomRepository.saveAll(venue.getRooms());
    venue.setRooms(new HashSet<>());

    if (edit.getRooms() != null) {
      for (RoomEditDto newRoom : edit.getRooms()) {
        Room room = roomMapper.roomEditDtoToRoom(newRoom);
        if (room.getSectors() != null) {
          for (Sector sector : room.getSectors()) {
            sector.setRoom(room);
            if (sector.getSeats() != null) {
              for (Seat seat : sector.getSeats()) {
                seat.setSector(sector);
              }
            }
          }
        }
        room.setVenue(venue);
        venue.getRooms().add(roomRepository.save(room));
      }


      /*
      List<RoomEditDto> editRooms = edit.getRooms().stream().filter(room -> room.getId() != null).toList();
      for (RoomEditDto editRoom : editRooms) {
        try {
          //roomRepository.deleteById(editRoom.getId());
          Room room = editRoom(editRoom);
          room.setVenue(venue);
          venue.getRooms().add(room);
        } catch (DataIntegrityViolationException ex) {
          errors.add("Failed to delete or update room " + editRoom.getName() + " because a performance is taking place in this room.");
        }
      }
       */

    }

    venueValidator.validateVenue(venue);

    venue = venueRepository.save(venue);
    List<String> errors = new ArrayList<>();

    if (!errors.isEmpty()) {
      throw new ValidationException("Failed to edit some or all rooms", errors);
    } else {
      return venue;
    }
  }

  private Room editRoom(RoomEditDto roomEditDto) {
    Room roomToEdit = roomRepository.findRoomById(roomEditDto.getId())
        .orElseThrow(() -> new NotFoundException("The room with the id " + roomEditDto.getId() + " does not exist"));
    // room already exists
    if (roomEditDto.getName() != null) {
      roomToEdit.setName(roomEditDto.getName());
    }
    if (roomEditDto.getRowSize() != null) {
      roomToEdit.setRowSize(roomEditDto.getRowSize());
    }
    if (roomEditDto.getColumnSize() != null) {
      roomToEdit.setColumnSize(roomEditDto.getColumnSize());
    }

    if (roomEditDto.getSectors() != null) {
      // reset current rooms in sector
      roomToEdit.getSectors().forEach(sector -> {
        sector.setRoom(null);
        sectorRepository.save(sector);
      });
      roomToEdit.setSectors(new HashSet<>());
      for (SectorEditDto sector : roomEditDto.getSectors()) {
        Sector editSector = editSector(sector);
        editSector.setRoom(roomToEdit);
        roomToEdit.getSectors().add(editSector);
      }
    }
    return roomToEdit;
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
      editSector.setSeats(new HashSet<>());
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
    List<Venue> venues = venueRepository.findAll();
    System.out.println(venues.size());
    System.out.println(venues);
    return venues;
  }

  @Override
  public Venue getById(long id) throws NotFoundException {
    LOGGER.trace("getLocationByID({})", id);
    Optional<Venue> venueOptional = venueRepository.findVenueById(id);
    if (venueOptional.isEmpty()) {
      throw new NotFoundException("Venue with id " + id + " could not be found");
    }
    return venueOptional.get();
  }

}

package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDtoSimple;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoomMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1/room")
public class RoomEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final RoomService service;
  private final RoomMapper mapper;

  public RoomEndpoint(RoomService service, RoomMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @DeleteMapping("{id}")
  @PermitAll
  public Room deleteRoom(@PathVariable long id) {
    LOGGER.info("deleteRoom({})", id);
    return service.deleteRoom(id);
  }

  @PatchMapping("{id}")
  @PermitAll
  public Room editRoom(@PathVariable long id, @RequestBody RoomEditDto toEdit) {
    LOGGER.info("editRoom({}, {})", id, toEdit);
    return this.service.editRoom(id, this.mapper.roomEditDtoToRoom(toEdit));
  }

  @PermitAll
  @GetMapping("{id}")
  public RoomDto getById(@PathVariable long id) {
    LOGGER.info("getById({})", id);
    return this.mapper.roomToRoomDto(this.service.getById(id));
  }

  @PermitAll
  @GetMapping("venue/{id}")
  public Stream<RoomDtoSimple> getRoomsByVenueId(@PathVariable long id) {
    LOGGER.info("getRoomsByVenueId({})", id);
    return this.service.getByVenueId(id).stream().map(this.mapper::roomToRoomDtoSimple);
  }

}

package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDtoSimple;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(uses = {SectorMapper.class})
public abstract class RoomMapper {

  @Autowired
  SectorMapper sectorMapper;

  public abstract RoomDto roomToRoomDto(Room room);

  @Named("roomDtoToRoom")
  public abstract Room roomDtoToRoom(RoomDto roomDto);

  @Named("roomEditDtoToRoom")
  public abstract Room roomEditDtoToRoom(RoomEditDto roomEditDto);

  @IterableMapping(qualifiedByName = "roomDtoToRoom")
  public abstract List<Room> roomDtosToRooms(List<RoomDto> roomDtos);

  @IterableMapping(qualifiedByName = "roomEditDtoToRoom")
  public abstract List<Room> roomEditDtosToRooms(List<RoomEditDto> roomEditDtos);

  public RoomDto roomToRoomDtoForPerformance(Room room, Long performanceId) {
    RoomDto roomGen = roomToRoomDto(room);
    if (performanceId == null) {
      return roomGen;
    }

    roomGen.setSectors(sectorMapper.sectorsToSectorDtosForPerformance(room.getSectors(), performanceId));

    return roomGen;
  }

  public abstract RoomDtoSimple roomToRoomDtoSimple(Room room);
}

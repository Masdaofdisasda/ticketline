package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {SeatMapper.class})
public interface RoomMapper {

  RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

  RoomDto roomToRommDto(Room room);

  @Named("roomDtoToRoom")
  Room roomDtoToRoom(RoomDto roomDto);

  @Named("roomEditDtoToRoom")
  Room roomEditDtoToRoom(RoomEditDto roomEditDto);

  @IterableMapping(qualifiedByName = "roomDtoToRoom")
  List<Room> roomDtosToRooms(List<RoomDto> roomDtos);

  @IterableMapping(qualifiedByName = "roomEditDtoToRoom")
  List<Room> roomEditDtosToRooms(List<RoomEditDto> roomEditDtos);
}

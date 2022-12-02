package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SeatMapper.class})
public interface RoomMapper {

  RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

  RoomDto roomToRommDto(Room room);

  Room roomDtoToRoom(RoomDto roomDto);
}

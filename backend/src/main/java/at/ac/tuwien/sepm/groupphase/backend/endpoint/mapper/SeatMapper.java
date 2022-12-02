package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SeatMapper {

  SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

  SeatDto seatToSeatDto(Seat seat);

  Seat seatDtoToSeat(SeatDto seatDto);
}

package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SeatMapper {

  SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

  SeatDto seatToSeatDto(Seat seat);

  Seat seatDtoToSeat(SeatDto seatDto);

  @Named("seatAddDtoToSeat")
  Seat seatAddDtoToSeat(SeatAddDto seatAddDto);

  Seat seatEditDtoToSeat(SeatEditDto seatEditDto);

  @IterableMapping(qualifiedByName = "seatAddDtoToSeat")
  List<Seat> seatAddDtosToSeat(List<SeatAddDto> seatAddDtoList);
}

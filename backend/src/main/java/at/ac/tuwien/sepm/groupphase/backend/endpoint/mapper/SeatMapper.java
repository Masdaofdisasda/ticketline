package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SeatMapper {

  SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

  @Mapping(target = "state", ignore = true)
  SeatDto seatToSeatDto(Seat seat);

  Seat seatDtoToSeat(SeatDto seatDto);

  @Named("seatAddDtoToSeat")
  Seat seatAddDtoToSeat(SeatAddDto seatAddDto);

  Seat seatEditDtoToSeat(SeatEditDto seatEditDto);

  @IterableMapping(qualifiedByName = "seatAddDtoToSeat")
  List<Seat> seatAddDtosToSeat(List<SeatAddDto> seatAddDtoList);

  @Named("seatToSeatDtoForPerformance")
  default SeatDto seatToSeatDtoForPerformance(Seat seat, Long performanceId) {
    SeatDto seatGen = seatToSeatDto(seat);

    if (performanceId == null) {
      return seatGen;
    }

    SeatDto.State state = SeatDto.State.BLOCKED;

    if (!seat.getTickets().isEmpty()) {
      for (Ticket ticket : seat.getTickets()) {
        if (!Objects.equals(ticket.getPerformance().getId(), performanceId)) {
          continue;
        }

        if (ticket.getBooking() == null) {
          state = SeatDto.State.FREE;
        } else if (ticket.getBooking().getBookingType() == BookingType.RESERVATION) {
          state = SeatDto.State.RESERVED;
        } else if (ticket.getBooking().getBookingType() == BookingType.PURCHASE) {
          state = SeatDto.State.TAKEN;
        } else {
          state = SeatDto.State.UNSET;
        }
      }
    }
    seatGen.setState(state);

    return seatGen;
  }

  @IterableMapping(qualifiedByName = "seatToSeatDtoForPerformance")
  default List<SeatDto> seatsToSeatDtosForPerformance(Set<Seat> seats, Long performanceId) {
    if (seats == null && performanceId == null) {
      return null;
    }

    ArrayList<SeatDto> arrayList = new ArrayList<SeatDto>();
    for (Seat seat : seats) {
      arrayList.add(seatToSeatDtoForPerformance(seat, performanceId));
    }

    return arrayList;
  }
}

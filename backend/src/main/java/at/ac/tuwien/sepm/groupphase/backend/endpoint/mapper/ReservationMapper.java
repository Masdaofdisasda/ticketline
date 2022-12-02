package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ReservationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationMapper {

  ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

  ReservationDto reservationToReservationDto(Reservation reservation);

  Reservation reservationDtoToReservation(ReservationDto reservationDto);
}

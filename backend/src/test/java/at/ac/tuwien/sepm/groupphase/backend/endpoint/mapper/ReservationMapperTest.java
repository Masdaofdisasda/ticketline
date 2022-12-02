package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ReservationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationMapperTest {
  ReservationMapper mapper = ReservationMapper.INSTANCE;

  Reservation reservation = Reservation.builder()
    .id(0L)
    .build();

  ReservationDto reservationDto = ReservationDto.builder()
    .id(1L)
    .build();

  @Test
  void reservationToReservationDto() {
    ReservationDto currentDto = mapper.reservationToReservationDto(reservation);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(reservation.getId());
    });
  }

  @Test
  void reservationDtoToReservation() {
    Reservation currentReservation = mapper.reservationDtoToReservation(reservationDto);
    assertAll(() -> {
      assertThat(currentReservation.getId()).isEqualTo(reservationDto.getId());
    });
  }
}
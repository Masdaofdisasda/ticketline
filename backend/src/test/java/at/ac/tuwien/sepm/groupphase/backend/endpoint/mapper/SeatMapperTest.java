package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SeatMapperTest {

  SeatMapper mapper = SeatMapper.INSTANCE;

  Seat seat = Seat.builder()
    .id(0L)
    .colNumber(0)
    .rowNumber(0)
    .build();

  SeatDto seatDto = SeatDto.builder()
    .id(1L)
    .colNumber(0)
    .rowNumber(2)
    .build();


  @Test
  void seatToSeatDto() {
    SeatDto currentDto = mapper.seatToSeatDto(seat);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(seat.getId());
      assertThat(currentDto.getColNumber()).isEqualTo(seat.getColNumber());
      assertThat(currentDto.getRowNumber()).isEqualTo(seat.getRowNumber());
    });
  }

  @Test
  void seatDtoToSeat() {
    Seat currentSeat = mapper.seatDtoToSeat(seatDto);
    assertAll(() -> {
      assertThat(currentSeat.getId()).isEqualTo(seatDto.getId());
      assertThat(currentSeat.getColNumber()).isEqualTo(seatDto.getColNumber());
      assertThat(currentSeat.getRowNumber()).isEqualTo(seatDto.getRowNumber());
    });
  }
}
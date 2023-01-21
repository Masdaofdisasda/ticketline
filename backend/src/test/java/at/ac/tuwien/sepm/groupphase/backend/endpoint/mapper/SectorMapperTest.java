package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SectorMapperTest {

  @Autowired
  private SectorMapper mapper;

  Seat seat = Seat.builder()
    .id(10L)
    .build();

  Sector sector = Sector.builder()
    .id(0L)
    .seats(List.of(seat))
    .build();

  SeatDto seatDto = SeatDto.builder()
    .id(10L)
    .state(SeatDto.State.UNSET)
    .build();
  SectorDto sectorDto = SectorDto.builder()
    .id(1L)
    .seats(List.of(seatDto))
    .build();

  @Test
  void sectorToSectorDto() {
    SectorDto currentDto = mapper.sectorToSectorDto(sector);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(sector.getId());
      assertThat(currentDto.getSeats()).isEqualTo(List.of(seatDto));
    });
  }

  @Test
  void sectorDtoToSector() {
    Sector currentSector = mapper.sectorDtoToSector(sectorDto);
    assertAll(() -> {
      assertThat(currentSector.getId()).isEqualTo(sectorDto.getId());
      assertThat(currentSector.getSeats()).isEqualTo(List.of(seat));
    });
  }
}
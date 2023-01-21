package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
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
class RoomMapperTest {

  Sector sector = Sector.builder()
    .id(11L)
    .seats(List.of(Seat.builder()
      .id(123L)
      .colNumber(1)
      .rowNumber(1)
      .build()))
    .build();
  Room room = Room.builder()
    .id(0L)
    .sectors(List.of(sector))
    .build();
  SectorDto sectorDto = SectorDto.builder()
    .id(11L)
    .seats(List.of(SeatDto.builder()
      .id(123L)
      .colNumber(1)
      .state(SeatDto.State.UNSET)
      .rowNumber(1)
      .build()))
    .build();
  RoomDto roomDto = RoomDto.builder()
    .id(1L)
    .sectors(List.of(sectorDto))
    .build();
  @Autowired
  private RoomMapper mapper;

  @Test
  void roomToRoomDto() {
    RoomDto currentDto = mapper.roomToRoomDto(room);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(room.getId());
      assertThat(currentDto.getSectors()).isEqualTo(List.of(sectorDto));
    });
  }

  @Test
  void roomDtoToRoom() {
    Room currentRoom = mapper.roomDtoToRoom(roomDto);
    assertThat(currentRoom.getId()).isEqualTo(roomDto.getId());
    assertThat(currentRoom.getSectors()).isEqualTo(List.of(sector));
  }
}
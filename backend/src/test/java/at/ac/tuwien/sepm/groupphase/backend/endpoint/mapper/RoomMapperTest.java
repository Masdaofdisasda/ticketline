package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
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

  @Autowired
  private RoomMapper mapper;

 SeatingPlan seatingPlan = SeatingPlan.builder()
   .id(10L)
   .build();

 Sector sector = Sector.builder()
   .id(11L)
   .build();
  Room room = Room.builder()
    .id(0L)
    .seatingPlans(List.of(seatingPlan))
    .sectors(List.of(sector))
    .build();

  SeatingPlanDto seatingPlanDto = SeatingPlanDto.builder()
    .id(10L)
    .build();

  SectorDto sectorDto = SectorDto.builder()
    .id(11L)
    .build();

  RoomDto roomDto = RoomDto.builder()
    .id(1L)
    .seatingPlans(List.of(seatingPlanDto))
    .sectors(List.of(sectorDto))
    .build();

  @Test
  void roomToRommDto() {
    RoomDto currentDto = mapper.roomToRommDto(room);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(room.getId());
      assertThat(currentDto.getSeatingPlans()).isEqualTo(List.of(seatingPlanDto));
      assertThat(currentDto.getSectors()).isEqualTo(List.of(sectorDto));
    });
  }

  @Test
  void roomDtoToRoom() {
    Room currentRoom = mapper.roomDtoToRoom(roomDto);
    assertAll(() -> {
      assertThat(currentRoom.getId()).isEqualTo(roomDto.getId());
      assertThat(currentRoom.getSeatingPlans()).isEqualTo(List.of(seatingPlan));
      assertThat(currentRoom.getSectors()).isEqualTo(List.of(sector));
    });
  }
}
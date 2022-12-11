package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SeatingPlanMapperTest {

  SeatingPlanMapper mapper = SeatingPlanMapper.INSTANCE;

  SeatingPlan seatingPlan = SeatingPlan.builder()
    .id(0L)
    .columns(10)
    .rows(10)
    .build();

  SeatingPlanDto seatingPlanDto = SeatingPlanDto.builder()
    .id(1L)
    .columns(11)
    .rows(11)
    .build();

  @Test
  void seatingPlanToSeatingPlanDto() {
    SeatingPlanDto currentDto = mapper.seatingPlanToSeatingPlanDto(seatingPlan);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(seatingPlan.getId());
      assertThat(currentDto.getColumns()).isEqualTo(seatingPlan.getColumns());
      assertThat(currentDto.getRows()).isEqualTo(seatingPlan.getRows());
    });
  }

  @Test
  void seatingPlanDtoToSeatingPlan() {
    SeatingPlan currentSeatingPlan = mapper.seatingPlanDtoToSeatingPlan(seatingPlanDto);
    assertAll(() -> {
      assertThat(currentSeatingPlan.getId()).isEqualTo(seatingPlanDto.getId());
      assertThat(currentSeatingPlan.getColumns()).isEqualTo(seatingPlanDto.getColumns());
      assertThat(currentSeatingPlan.getRows()).isEqualTo(seatingPlanDto.getRows());
    });
  }
}
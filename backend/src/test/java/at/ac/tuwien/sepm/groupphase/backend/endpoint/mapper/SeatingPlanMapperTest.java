package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SeatingPlanMapperTest {
/*
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
  */

}
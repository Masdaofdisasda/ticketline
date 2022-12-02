package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class EventMapperTest {

  @Autowired
  private EventMapper mapper;

  Performance performance = Performance.builder()
    .id(7L)
    .startDate(LocalDate.now())
    .endDate(LocalDate.MAX)
    .build();

  Event event = Event.builder()
    .id(0L)
    .startDate(LocalDate.MIN)
    .endDate(LocalDate.MAX)
    .category("HIPHOP")
    .name("HOPHIPEVENT")
    .performances(List.of(performance))
    .build();

  PerformanceDto performanceDto = PerformanceDto.builder()
    .id(7L)
    .startDate(LocalDate.now())
    .endDate(LocalDate.MAX)
    .build();

  EventDto eventDto = EventDto.builder()
    .id(1L)
    .startDate(LocalDate.EPOCH)
    .endDate(LocalDate.now())
    .category("POP")
    .name("POPEVENT")
    .performances(List.of(performanceDto))
    .build();

  @Test
  void eventToEventDto() {
    EventDto currentDto = mapper.eventToEventDto(event);

    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(event.getId());
      assertThat(currentDto.getStartDate()).isEqualTo(event.getStartDate());
      assertThat(currentDto.getEndDate()).isEqualTo(event.getEndDate());
      assertThat(currentDto.getCategory()).isEqualTo(event.getCategory());
      assertThat(currentDto.getName()).isEqualTo(event.getName());
      assertThat(currentDto.getPerformances()).isEqualTo(List.of(performanceDto));
    });
  }

  @Test
  void eventDtoToEvent() {
    Event currentevent = mapper.eventDtoToEvent(eventDto);

    assertAll(() -> {
      assertThat(currentevent.getId()).isEqualTo(eventDto.getId());
      assertThat(currentevent.getStartDate()).isEqualTo(eventDto.getStartDate());
      assertThat(currentevent.getEndDate()).isEqualTo(eventDto.getEndDate());
      assertThat(currentevent.getCategory()).isEqualTo(eventDto.getCategory());
      assertThat(currentevent.getName()).isEqualTo(eventDto.getName());
      assertThat(currentevent.getPerformances()).isEqualTo(List.of(performance));
    });
  }
}
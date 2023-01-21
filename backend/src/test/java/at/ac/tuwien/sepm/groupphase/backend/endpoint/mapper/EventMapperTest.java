package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    .startDate(LocalDateTime.of(2022, 12, 11, 19, 0))
    .endDate(LocalDateTime.MAX)
    .room(Room.builder().build())
    .build();

  Event event = Event.builder()
    .id(0L)
    .startDate(LocalDateTime.of(2022, 12, 11, 19, 0))
    .endDate(LocalDateTime.of(2022, 12, 12, 1, 30))
    .category("HIPHOP")
    .name("HOPHIPEVENT")
    .performances(List.of(performance))
    .build();

  PerformanceDto performanceDto = PerformanceDto.builder().id(7L).startDate(LocalDateTime.of(2022, 12, 11, 19, 0)).endDate(LocalDateTime.MAX)
    .room(RoomDto.builder()
      .sectors(new ArrayList<>())
      .build())
    .build();

  EventDto eventDto = EventDto.builder()
    .id(1L)
    .startDate(LocalDateTime.of(2022, 12, 11, 19, 0))
    .endDate(LocalDateTime.of(2022, 12, 12, 1, 30))
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
      assertThat(currentDto.getPerformances()).isEqualTo(new ArrayList<>(List.of(performanceDto)));
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
      assertThat(currentevent.getPerformances()).isEqualTo(new ArrayList<>(List.of(performance)));
    });
  }
}
package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchRequest;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class EventRepositoryTest {

  @Autowired
  private EventRepository eventRepository;

  @Test
  void findForFilter_withQueryParams_shouldReturnFiveHits() {
    for (int i = 1; i <= 10; i++) {
      eventRepository.save(Event.builder()
        .id((long) i)
        .name("Test Event" + i)
        .category("Category " + i)
        .startDate(LocalDateTime.now().plusMinutes(10))
        .endDate(LocalDateTime.now().plusHours(3))
        .build());
    }
    assertThat(eventRepository.findAll().size()).isEqualTo(10);

    Page<Event> result = eventRepository.findForFilter(new EventSearchRequest(null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      0, 5), PageRequest.of(0, 5));

    assertAll(
      () -> assertEquals(result.getTotalElements(), 10),
      () -> assertEquals(result.getTotalPages(), 2),
      () -> assertEquals(result.getNumberOfElements(), 5)
    );
  }

  @Test
  void saveEvent() {
    List<Event> eventList = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      Event event = Event.builder()
        .id((long) i)
        .name("Test Event" + i)
        .category("Category " + i)
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusHours(3))
        .build();
      eventList.add(event);
      eventRepository.save(event);
      System.out.println("here");
    }

    assertThat(eventRepository.findAll().size()).isEqualTo(3);
    for (long i = 1; i <= 3; i++) {
      assertThat(eventRepository.findById(i).equals(eventList.get((int) i - 1)));
    }

  }

  void findTopOfMonth() {
    // TODO: 11.12.22 add testcase here
  }
}
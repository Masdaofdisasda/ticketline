package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventFixture {

  public static Event getBuildEvent(int i) {
    switch (i % 3) {
      case 0 -> {
        return buildEvent1().toBuilder()
          .id((long) i)
          .build();
      }
      case 1 -> {
        return buildEvent2().toBuilder()
          .id((long) i)
          .build();
      }
      default -> {
        return buildEvent3().toBuilder()
          .id((long) i)
          .build();
      }
    }
  }

  public static Event buildEvent1() {
    return Event.builder()
      .id(1L)
      .name("To Pool for Cool")
      .category("Wet T-Shirt Contest")
      .startDate(LocalDateTime.now().plusHours(1))
      .endDate(LocalDateTime.now().plusHours(4))
      .news(new ArrayList<>())
      .performances(new ArrayList<>())
      .build();
  }

  public static Event buildEvent2() {
    return Event.builder()
      .id(2L)
      .name("Angular Deep Dive")
      .category("Tutorial")
      .startDate(LocalDateTime.now().plusHours(1))
      .endDate(LocalDateTime.now().plusHours(4))
      .news(new ArrayList<>())
      .performances(new ArrayList<>())
      .build();
  }

  public static Event buildEvent3() {
    return Event.builder()
      .id(3L)
      .name("The Big Drowning")
      .category("Movie")
      .startDate(LocalDateTime.now().plusHours(1))
      .endDate(LocalDateTime.now().plusHours(4))
      .news(new ArrayList<>())
      .performances(new ArrayList<>())
      .build();
  }
}

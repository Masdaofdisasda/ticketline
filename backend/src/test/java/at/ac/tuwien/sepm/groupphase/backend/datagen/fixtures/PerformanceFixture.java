package at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;

import java.time.LocalDateTime;
import java.util.Set;

import static at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.ArtistFixture.buildArtist1;
import static at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.ArtistFixture.buildArtist2;
import static at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.ArtistFixture.buildArtist3;
import static at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.EventFixture.buildEvent1;
import static at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.EventFixture.buildEvent2;
import static at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures.EventFixture.buildEvent3;

public class PerformanceFixture {

  public static Performance buildPerformance1() {
    return Performance.builder()
      .id(1L)
      .startDate(LocalDateTime.now())
      .endDate(LocalDateTime.now().plusDays(7))
      .room(RoomFixture.buildRoom1())
      .artists(Set.of(buildArtist1()))
      .event(buildEvent1())
      .build();
  }

  public static Performance buildPerformance2() {
    return Performance.builder()
      .id(2L)
      .startDate(LocalDateTime.now().plusDays(1))
      .endDate(LocalDateTime.now().plusDays(7))
      .room(RoomFixture.buildRoom2())
      .artists(Set.of(buildArtist2()))
      .event(buildEvent2())
      .build();
  }

  public static Performance buildPerformance3() {
    return Performance.builder()
      .id(3L)
      .startDate(LocalDateTime.now().plusDays(2))
      .endDate(LocalDateTime.now().plusDays(7))
      .artists(Set.of(buildArtist3()))
      .room(RoomFixture.buildRoom3())
      .event(buildEvent3())
      .build();
  }
}

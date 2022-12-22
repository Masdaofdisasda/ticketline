package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.ArtistFixture.buildArtist1;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.ArtistFixture.buildArtist2;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.ArtistFixture.buildArtist3;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.EventFixture.buildEvent1;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.EventFixture.buildEvent2;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.EventFixture.buildEvent3;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.LocationFixture.buildLocation1;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.LocationFixture.buildLocation2;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.LocationFixture.buildLocation3;

public class PerformanceFixture {

  public static Performance buildPerformance1() {
    return Performance.builder()
      .id(1L)
      .startDate(LocalDateTime.now())
      .endDate(LocalDateTime.now().plusDays(7))
      .location(buildLocation1())
      .artist(buildArtist1())
      .tickets(new ArrayList<>())
      .event(buildEvent1())
      .build();
  }

  public static Performance buildPerformance2() {
    return Performance.builder()
      .id(2L)
      .startDate(LocalDateTime.now().plusDays(1))
      .endDate(LocalDateTime.now().plusDays(7))
      .location(buildLocation2())
      .artist(buildArtist2())
      .tickets(new ArrayList<>())
      .event(buildEvent2())
      .build();
  }

  public static Performance buildPerformance3() {
    return Performance.builder()
      .id(3L)
      .startDate(LocalDateTime.now().plusDays(2))
      .endDate(LocalDateTime.now().plusDays(7))
      .artist(buildArtist3())
      .location(buildLocation3())
      .tickets(new ArrayList<>())
      .event(buildEvent3())
      .build();
  }
}

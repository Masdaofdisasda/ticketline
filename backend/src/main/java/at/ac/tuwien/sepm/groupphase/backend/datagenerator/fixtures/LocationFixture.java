package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;

import java.util.ArrayList;

public class LocationFixture {

  public static Location getBuildLocation(int i) {
    switch (i % 3) {
      case 0 -> {
        return buildLocation1();
      }
      case 1 -> {
        return buildLocation2();
      }
      default -> {
        return buildLocation3();
      }
    }
  }

  public static Location buildLocation1() {
    return Location.builder()
      .id(1L)
      .city("Vienna")
      .country("Austria")
      .zip(1220)
      .street("Belongs to the Street")
      .performances(new ArrayList<>())
      .build();
  }

  public static Location buildLocation2() {
    return Location.builder()
      .id(2L)
      .city("Salzburg")
      .country("Austria")
      .zip(3312)
      .street("Belongs to the Street")
      .performances(new ArrayList<>())
      .build();
  }

  public static Location buildLocation3() {
    return Location.builder()
      .id(3L)
      .city("Berlin")
      .country("Germany")
      .zip(8492)
      .street("Belongs to the Street")
      .performances(new ArrayList<>())
      .build();
  }
}

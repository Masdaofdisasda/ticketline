package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;

import java.util.ArrayList;

public class VenueFixture {

  public static Venue getBuildVenue(int i) {
    switch (i % 3) {
      case 0 -> {
        return buildVenue1();
      }
      case 1 -> {
        return buildVenue2();
      }
      default -> {
        return buildVenue3();
      }
    }
  }

  public static Venue buildVenue1() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Venue.builder()
      .id(1L)
      .city("Vienna")
      .country("Austria")
      .zipCode("1220")
      .street("Belongs to the Street")
      .performances(new ArrayList<>())
      .build();
  }

  public static Venue buildVenue2() {
    return Venue.builder()
      .id(2L)
      .city("Salzburg")
      .country("Austria")
      .zipCode("3312")
      .street("Belongs to the Street")
      .performances(new ArrayList<>())
      .build();
  }

  public static Venue buildVenue3() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Venue.builder()
      .id(3L)
      .city("Berlin")
      .country("Germany")
      .zipCode("8492")
      .street("Belongs to the Street")
      .performances(new ArrayList<>())
      .build();
  }
}

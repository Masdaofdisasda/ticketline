package at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;

import javax.annotation.PostConstruct;
import java.util.HashSet;

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

  @PostConstruct
  public static Venue buildVenue1() {
    return Venue.builder()
      .id(1L)
      .name("Stadthalle")
      .city("Vienna")
      .houseNumber("5")
      .country("Austria")
      .zipCode("1220")
      .street("Belongs to the Street")
      .rooms(new HashSet<>())
      .build();
  }

  @PostConstruct
  public static Venue buildVenue2() {
    return Venue.builder()
      .id(2L)
      .name("Stadthalle in Salzburg")
      .city("Salzburg")
      .country("Austria")
      .zipCode("3312")
      .street("Belongs to the Street")
      .houseNumber("5")
      .rooms(new HashSet<>())
      .build();
  }

  @PostConstruct
  public static Venue buildVenue3() {
    return Venue.builder()
      .id(3L)
      .name("Stadthalle in Berlin")
      .city("Berlin")
      .country("Germany")
      .zipCode("8492")
      .street("Belongs to the Street")
      .houseNumber("5")
      .rooms(new HashSet<>())
      .build();
  }
}

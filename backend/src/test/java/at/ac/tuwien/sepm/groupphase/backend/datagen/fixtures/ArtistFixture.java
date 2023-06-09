package at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;

import java.util.HashSet;

public class ArtistFixture {

  public static Artist getBuildArtist(int i) {
    switch (i % 3) {
      case 0 -> {
        return buildArtist1();
      }
      case 1 -> {
        return buildArtist2();
      }
      default -> {
        return buildArtist3();
      }
    }
  }

  public static Artist buildArtist1() {
    return Artist.builder()
      .id(1L)
      .name("Queen Elizabeth")
      .performances(new HashSet<>())
      .build();
  }

  public static Artist buildArtist2() {
    return Artist.builder()
      .id(2L)
      .name("Gandi on the Beat")
      .performances(new HashSet<>())
      .build();
  }

  public static Artist buildArtist3() {
    return Artist.builder()
      .id(3L)
      .name("Taylor Swift")
      .performances(new HashSet<>())
      .build();
  }
}

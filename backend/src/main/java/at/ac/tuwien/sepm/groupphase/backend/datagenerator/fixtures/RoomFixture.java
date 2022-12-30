package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Room;

import java.util.ArrayList;

import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.VenueFixture.buildVenue1;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.VenueFixture.buildVenue2;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.VenueFixture.buildVenue3;

public class RoomFixture {

  public static Room getBuildRoom(int i) {
    switch (i % 9) {
      case 0 -> {
        return buildRoom1();
      }
      case 1 -> {
        return buildRoom2();
      }
      case 2 -> {
        return buildRoom3();
      }
      case 3 -> {
        return buildRoom4();
      }
      case 4 -> {
        return buildRoom5();
      }
      case 5 -> {
        return buildRoom6();
      }
      case 6 -> {
        return buildRoom7();
      }
      case 7 -> {
        return buildRoom8();
      }
      default -> {
        return buildRoom9();
      }
    }
  }

  public static Room buildRoom1() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(1L)
      .venue(buildVenue1())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(10)
      .rowSize(10)
      .name("Hall1")
      .build();
  }

  public static Room buildRoom2() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(2L)
      .venue(buildVenue1())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(20)
      .rowSize(20)
      .name("Hall2")
      .build();
  }

  public static Room buildRoom3() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(3L)
      .venue(buildVenue1())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(30)
      .rowSize(30)
      .name("Hall3")
      .build();
  }

  public static Room buildRoom4() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(4L)
      .venue(buildVenue2())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(10)
      .rowSize(10)
      .name("Hall4")
      .build();
  }

  public static Room buildRoom5() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(5L)
      .venue(buildVenue2())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(20)
      .rowSize(20)
      .name("Hall5")
      .build();
  }

  public static Room buildRoom6() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(6L)
      .venue(buildVenue2())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(30)
      .rowSize(30)
      .name("Hall6")
      .build();
  }

  public static Room buildRoom7() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(7L)
      .venue(buildVenue3())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(10)
      .rowSize(10)
      .name("Hall7")
      .build();
  }

  public static Room buildRoom8() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(8L)
      .venue(buildVenue3())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(20)
      .rowSize(20)
      .name("Hall8")
      .build();
  }

  public static Room buildRoom9() {
    return at.ac.tuwien.sepm.groupphase.backend.entity.Room.builder()
      .id(9L)
      .venue(buildVenue3())
      .performances(new ArrayList<>())
      .sectors(new ArrayList<>())
      .columnSize(30)
      .rowSize(30)
      .name("Hall9")
      .build();
  }
}

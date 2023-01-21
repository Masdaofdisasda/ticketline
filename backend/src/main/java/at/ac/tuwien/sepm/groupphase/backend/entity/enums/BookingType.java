package at.ac.tuwien.sepm.groupphase.backend.entity.enums;

import lombok.Getter;

@Getter
public enum BookingType {

  RESERVATION("Reservation"),
  PURCHASE("Purchase");

  private final String type;

  BookingType(String type) {
    this.type = type;
  }
}

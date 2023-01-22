package at.ac.tuwien.sepm.groupphase.backend.entity.enums;

import lombok.Getter;

@Getter
public enum DocumentType {

  TICKETS("Tickets"),
  RECEIPT("Receipt"),
  CANCELLATION("Cancellation");

  private final String type;

  DocumentType(String type) {
    this.type = type;
  }
}

package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketFixture {
  public static List<Ticket> getBuildTickets(int counter) {
    List<Ticket> tickets = new ArrayList<>();
    for (int i = 1; i <= counter; i++) {
      switch (i % 3) {
        case 0 -> tickets.add(buildTicket1().toBuilder()
          .build());
        case 1 -> tickets.add(buildTicket2().toBuilder()
          .build());
        default -> tickets.add(buildTicket3().toBuilder()
          .build());
      }
    }
    return tickets;
  }

  public static Ticket buildTicket1() {
    return Ticket.builder()
      .price(50.20f)
      .build();
  }

  public static Ticket buildTicket2() {
    return Ticket.builder()
      .price(19.55f)
      .build();
  }

  public static Ticket buildTicket3() {
    return Ticket.builder()
      .price(100.50f)
      .build();
  }
}
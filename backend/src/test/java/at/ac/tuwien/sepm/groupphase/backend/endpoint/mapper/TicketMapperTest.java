package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class TicketMapperTest {

  @Autowired
  private TicketMapper mapper;

  Seat seat = Seat.builder()
    .id(10L)
    .build();

  Ticket ticket = Ticket.builder()
    .id(0L)
    .price(BigDecimal.valueOf(20))
    .seat(seat)
    .build();

  SeatDto seatDto = SeatDto.builder()
    .id(10L)
    .build();

  TicketDto ticketDto = TicketDto.builder()
    .id(1L)
    .price(BigDecimal.valueOf(30))
    //.seat(seatDto)
    .build();

  @Test
  void ticketToTicketDto() {
    TicketDto currentDto = mapper.ticketToTicketDto(ticket);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(ticket.getId());
      assertThat(currentDto.getPrice()).isEqualTo(ticket.getPrice());
      //assertThat(currentDto.getSeat()).isEqualTo(seatDto);
    });
  }

  @Test
  void ticketDtoToTicket() {
    Ticket currentTicket = mapper.ticketDtoToTicket(ticketDto);
    assertAll(() -> {
      assertThat(currentTicket.getId()).isEqualTo(ticketDto.getId());
      assertThat(currentTicket.getPrice()).isEqualTo(ticketDto.getPrice());
      //assertThat(currentTicket.getSeat()).isEqualTo(seat);
    });
  }
}
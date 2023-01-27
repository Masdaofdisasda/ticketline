package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {SeatMapper.class})
public interface TicketMapper {

  TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

  @Named("ticketToTicketDto")
  TicketDto ticketToTicketDto(Ticket ticket);

  TicketSimpleDto ticketToTicketSimpleDto(Ticket ticket);

  Ticket ticketDtoToTicket(TicketDto ticketDto);


  @IterableMapping(qualifiedByName = "ticketToTicketDto")
  List<TicketDto> ticketsToTicketDtos(List<Ticket> ticketList);
}

package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {PerformanceMapper.class})
public interface EventMapper {

  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  @Named("eventToEventDto")
  EventDto eventToEventDto(Event event);

  @IterableMapping(qualifiedByName = "eventToEventDto")
  List<EventDto> eventToEventDto(List<Event> message);

  Event eventDtoToEvent(EventDto eventDto);
}

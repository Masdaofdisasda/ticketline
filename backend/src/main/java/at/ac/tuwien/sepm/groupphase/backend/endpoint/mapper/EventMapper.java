package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PerformanceMapper.class})
public interface EventMapper {

  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  EventDto eventToEventDto(Event event);

  Event eventDtoToEvent(EventDto eventDto);
}

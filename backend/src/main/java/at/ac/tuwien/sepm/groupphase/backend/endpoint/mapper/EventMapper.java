package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDtoExtended;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDtoSimple;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {PerformanceMapper.class})
public interface EventMapper {

  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  @Named("eventToEventDto")
  EventDto eventToEventDto(Event event);

  @IterableMapping(qualifiedByName = "eventToEventDto")
  List<EventDto> eventToEventDto(List<Event> events);

  @Named("eventSimpleEventDto")
  @Mapping(source = "performances", target = "artistName", qualifiedByName = "getArtistName")
  @Mapping(source = "performances", target = "eventHallName", qualifiedByName = "getEventHallName")
  @Mapping(source = "performances", target = "venueName", qualifiedByName = "getVenueName")
  EventDtoSimple eventSimpleEventDto(Event event);

  @Named("eventToExtendedEventDto")
  @Mapping(source = "performances", target = "artistName", qualifiedByName = "getArtistName")
  @Mapping(source = "performances", target = "eventHallName", qualifiedByName = "getEventHallName")
  @Mapping(source = "performances", target = "venueName", qualifiedByName = "getVenueName")
  EventDtoExtended eventToExtendedEventDto(Event event);

  @IterableMapping(qualifiedByName = "eventToExtendedEventDto")
  List<EventDtoExtended> eventToExtendedEventDto(List<Event> events);

  Event eventDtoToEvent(EventDto eventDto);

  Event eventDtoToEvent(EventCreateDto eventDto);

  @IterableMapping(qualifiedByName = "eventSimpleEventDto")
  List<EventDtoSimple> eventToSimpleEventDto(Set<Event> events);

  @Named("getArtistName")
  default String getArtistName(Set<Performance> performances) {
    List<String> artistNames = performances.stream()
        .map(Performance::getArtists)
        .flatMap(Collection::stream)
        .distinct()
        .filter(Objects::nonNull)
        .map(Artist::getName).toList();

    return String.join(",", artistNames);
  }

  @Named("getEventHallName")
  default String getEventHallName(Set<Performance> performances) {
    List<String> eventHallNames = performances.stream().map(Performance::getRoom)
        .filter(Objects::nonNull)
        .distinct().map(Room::getName).toList();

    return String.join(",", eventHallNames);
  }

  @Named("getVenueName")
  default String getVenueName(Set<Performance> performances) {
    List<String> venueNames = performances.stream()
        .map(Performance::getRoom)
        .map(Room::getVenue)
        .filter(Objects::nonNull)
        .distinct().map(Venue::getName).toList();

    return String.join(",", venueNames);
  }

}

package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VenueMapper {

  VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

  Venue venueAddDtoToVenue(VenueAddDto venueAddDto);

  Venue venueDtoToVenue(VenueDto venueDto);

  VenueDto venueToVenueDto(Venue venue);

  @Mapping(target = "rooms", ignore = true)
  VenueDto simpleVenueToVenueDto(Venue venue);
}

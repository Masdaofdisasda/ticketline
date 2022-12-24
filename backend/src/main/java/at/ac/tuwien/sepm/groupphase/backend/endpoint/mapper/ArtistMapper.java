package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArtistMapper {

  ArtistMapper INSTANCE = Mappers.getMapper(ArtistMapper.class);

  Artist artistDtoToArtist(ArtistDto artistDto);

  ArtistDto artistToArtistDto(Artist artist);
}

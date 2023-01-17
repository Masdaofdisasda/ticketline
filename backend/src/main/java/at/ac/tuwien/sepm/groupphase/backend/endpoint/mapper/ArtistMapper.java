package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ArtistMapper {

  ArtistMapper INSTANCE = Mappers.getMapper(ArtistMapper.class);

  @Named("artistDtoToArtist")
  Artist artistDtoToArtist(ArtistDto artistDto);

  @IterableMapping(qualifiedByName = "artistDtoToArtist")
  List<Artist> artistDtoToArtist(List<ArtistDto> artistDtos);

  @Named("artistToArtistDto")
  ArtistDto artistToArtistDto(Artist artist);

  @IterableMapping(qualifiedByName = "artistToArtistDto")
  List<ArtistDto> artistsToArtistDtos(List<Artist> artists);
}

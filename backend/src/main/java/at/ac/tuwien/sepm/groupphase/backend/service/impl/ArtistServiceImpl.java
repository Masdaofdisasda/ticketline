package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.validator.ArtistValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@Service
public class ArtistServiceImpl implements ArtistService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final ArtistRepository artistRepository;
  private final ArtistMapper artistMapper;

  private final ArtistValidator artistValidator;

  public ArtistServiceImpl(ArtistRepository artistRepository, ArtistMapper artistMapper, ArtistValidator artistValidator) {
    this.artistRepository = artistRepository;
    this.artistMapper = artistMapper;
    this.artistValidator = artistValidator;
  }


  @Override
  public Artist create(ArtistDto artist) throws ValidationException {
    artistValidator.validateArtist(artist);
    return artistRepository.save(artistMapper.artistDtoToArtist(artist));
  }

  @Override
  public Stream<ArtistDto> findAll() {
    return artistRepository.findAll().stream().map(artist -> artistMapper.artistToArtistDto(artist));
  }

  @Override
  public Stream<ArtistDto> filterByName(ArtistDto artist) {
    return artistRepository.findArtistsByNameStartingWith(artist.getName()).stream().map(artistDto -> artistMapper.artistToArtistDto(artistDto));
  }
}

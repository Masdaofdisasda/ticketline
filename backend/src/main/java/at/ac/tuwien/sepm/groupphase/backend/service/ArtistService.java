package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import java.util.stream.Stream;

public interface ArtistService {

  /**
   * saves Artist to database.
   *
   * @param artist artist to be created
   * @return created event
   * @throws ValidationException when validation for ArtistDto fails
   */
  Artist create(ArtistDto artist) throws ValidationException;


  /**
   * findAll returns List of all artists.
   *
   * @return list of artists from repo
   */
  Stream<ArtistDto> findAll();


  /**
   * findAll returns List of artists matching containing the name of given artist.
   *
   * @param artist artist with name to be filtered for
   * @return list of artists from repo
   */
  Stream<ArtistDto> filterByName(ArtistDto artist);

}

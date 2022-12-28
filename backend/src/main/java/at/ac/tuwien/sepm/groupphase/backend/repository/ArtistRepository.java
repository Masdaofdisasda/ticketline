package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

  /**
   * findArtistsByName returns list of artists.
   *
   * @param name of artist to be returned
   * @return list of artists with matching name
   */
  List<Artist> findArtistsByName(String name);

  List<Artist> findArtistsByNameStartingWith(String string);

  List<Artist> findAll();
}

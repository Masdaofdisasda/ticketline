package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
  @Query("SELECT v FROM Venue v"
      + " LEFT JOIN FETCH v.rooms r"
      + " LEFT JOIN FETCH r.sectors s"
      + " LEFT JOIN FETCH s.priceCategory pr"
      + " LEFT JOIN FETCH s.seats"
      + " WHERE v.id = :venueId")
  Optional<Venue> findVenueById(@Param("venueId") long id);
}

package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

  @Modifying
  @Query("DELETE FROM Room r where r.venue.id = :venueId")
  void deleteByVenueId(@Param("venueId") long id);

  @Query("SELECT r FROM Room r"
    + " LEFT JOIN FETCH r.sectors WHERE r.id = :id")
  Optional<Room> findRoomById(@Param("id") Long id);

  @Query("SELECT DISTINCT r FROM Room r"
    + " LEFT JOIN FETCH r.sectors sectors"
    + " LEFT JOIN FETCH sectors.priceCategory priceCat"
    + " LEFT JOIN FETCH sectors.seats"
    + " WHERE r.id = :id")
  Optional<Room> findRoomWithSectoryById(@Param("id") Long id);

  @Query("FROM Room room JOIN FETCH room.performances p WHERE room.venue.id = :id")
  List<Room> findAllByVenueWithPerformancesId(@Param("id") Long id);

  @Query("FROM Room room WHERE room.venue.id = :id")
  List<Room> findAllByVenueId(@Param("id") Long id);

}

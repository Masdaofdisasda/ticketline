package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
  Set<Booking> getBookingByUserId(@Param("userId") Long userId);

  @Query("SELECT b FROM Booking b JOIN FETCH b.tickets WHERE b.id = :id")
  Optional<Booking> findBookingById(@Param("id") Long id);
}

package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Query("SELECT t FROM Ticket t WHERE t.seat.id = :seatId")
  Ticket findBySeatId(@Param("seatId") Long seatId);

  @Query("SELECT t FROM Ticket t WHERE t.seat.id IN :seatIds")
  List<Ticket> findBySeatIds(@Param("seatIds") List<Long> seatIds);
}

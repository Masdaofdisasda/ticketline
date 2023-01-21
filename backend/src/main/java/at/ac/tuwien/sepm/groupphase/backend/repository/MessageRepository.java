package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  /**
   * findAllPagable return messages by descending publishing date.
   *
   * @param pageable holds page information like index and pagesize
   * @return page with matching events
   */
  @Query("SELECT me FROM Message me"
    + " ORDER BY me.publishedAt desc")
  Page<Message> findAllPagable(Pageable pageable);
}

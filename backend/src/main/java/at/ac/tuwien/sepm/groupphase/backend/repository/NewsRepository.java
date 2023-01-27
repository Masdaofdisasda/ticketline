package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

  /**
   * findAllPagable return news by descending publishing date.
   *
   * @param pageable holds page information like index and pagesize
   * @return page with matching news
   */
  @Query("SELECT me FROM News me"
    + " ORDER BY me.publishedAt desc")
  Page<News> findAllPagable(Pageable pageable);

  /**
   * findAllPagable return news by descending publishing date that have been released.
   *
   * @param pageable holds page information like index and pagesize
   * @return page with matching events
   */
  @Query("SELECT me FROM News me"
    + " WHERE me.publishedAt < CURRENT_DATE"
    + " ORDER BY me.publishedAt desc")
  Page<News> findAllReleasedNewsPagable(Pageable pageable);

  /**
   * findAllPagable return news by descending publishing date.
   *
   * @param pageable holds page information like index and pagesize
   * @return page with unread news
   */
  @Query("SELECT n FROM News n"
    + " WHERE NOT EXISTS ("
    + " SELECT b from n.hasSeen b where b.id = ?1)"
    + " ORDER BY n.publishedAt desc")
  Page<News> findAllUnreadPagable(Long userId, Pageable pageable);

  @Query("SELECT n FROM News n LEFT JOIN FETCH n.hasSeen WHERE n.id = :id")
  Optional<News> findNewsById(@Param("id") Long id);
}

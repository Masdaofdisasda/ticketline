package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.domain.Page;

public interface NewsService {

  Page<News> findAllPaginated(PageDto pageDto);

  /**
   * Find a single message entry by id.
   *
   * @param id the id of the message entry
   * @return the message entry
   */
  News findOne(Long id);

  /**
   * Mark a message as seen by a userID.
   *
   * @param userId the userId of the newsRead entry
   * @param newsId the id of the news
   * @return the message entry
   */
  News hasSeen(Long userId, Long newsId);

  /**
   * Publish a single message entry.
   *
   * @param news to publish
   * @return published message entry
   */
  News publishMessage(News news);

}

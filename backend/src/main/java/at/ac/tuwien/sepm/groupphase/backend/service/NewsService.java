package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.domain.Page;

public interface NewsService {

  /**
   * Finds a page of news entries.
   *
   * @param pageDto describing which page to fetch
   * @return Page of news
   */
  Page<News> findAllPaginated(PageDto pageDto);

  /**
   * Finds a page of news entries that were release before today.
   *
   * @param pageDto describing which page to fetch
   * @return Page of news
   */
  Page<News> findAllReleasedNewsPaginated(PageDto pageDto);

  /**
   * Finds a page of news entries which the user hasn't seen.
   *
   * @param pageDto describing which page to fetch
   * @param userId  id of the current user
   * @return Page of news
   */
  Page<News> findAllUnreadPaginated(Long userId, PageDto pageDto);

  /**
   * Finds a page of news entries which the user hasn't seen and which haven't been released.
   *
   * @param pageDto describing which page to fetch
   * @param userId  id of the current user
   * @return Page of news
   */
  Page<News> findAllReleasedUnreadPaginated(Long userId, PageDto pageDto);

  /**
   * Find a single message entry by id.
   *
   * @param id the id of the message entry
   * @return the message entry
   */
  News findOne(Long id);

  /**
   * Mark a news entry as seen by a user.
   *
   * @param userId the userId of the newsRead entry
   * @param news   the id of the news
   */
  void markAsSeen(Long userId, News news);

  /**
   * Publish a single message entry.
   *
   * @param news to publish
   * @return published message entry
   */
  News publishMessage(News news);

}

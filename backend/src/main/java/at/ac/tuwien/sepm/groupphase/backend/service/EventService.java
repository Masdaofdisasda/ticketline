package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Page;

public interface EventService {

  /**
   * findAll returns page object with events.
   *
   * @param pageDto holds page infos
   * @return page with events returned from repo
   */
  Page<Event> findAll(PageDto pageDto);

  /**
   * filter returns page object with events matching given criteria.
   *
   * @param eventSearchRequest contains filtering criteria
   * @return page with matching events returned from repo
   */
  Page<Event> filter(EventSearchRequest eventSearchRequest);

  /**
   * topOfMonth returns page object with events ordered desc after the most sails.
   *
   * @param pageDto holds page infos
   * @return page with events returned from repo
   */
  Page<Event> topOfMonth(PageDto pageDto);
}

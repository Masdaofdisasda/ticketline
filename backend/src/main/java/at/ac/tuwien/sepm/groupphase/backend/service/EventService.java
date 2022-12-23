package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.data.domain.Page;

import java.util.List;

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

  /**
   * saves Event to database.
   *
   * @param event event to be created
   * @return created event
   * @throws ValidationException when validation for EventDto fails
   */
  public Event create(EventDto event) throws ValidationException;

  /**
   * get Event categories from database.
   *
   * @return list of event categories
   */
  List<String> getCategories();
}

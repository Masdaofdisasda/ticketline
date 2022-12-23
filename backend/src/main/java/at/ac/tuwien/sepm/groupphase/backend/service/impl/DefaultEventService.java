package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class DefaultEventService implements EventService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;

  private final EventValidator eventValidator;

  public DefaultEventService(EventRepository eventRepository, EventMapper eventMapper, EventValidator eventValidator) {
    this.eventRepository = eventRepository;
    this.eventMapper = eventMapper;
    this.eventValidator = eventValidator;
  }

  /**
   * findAll returns page object with events.
   *
   * @param pageDto holds page infos
   * @return page with events returned from repo
   */
  @Override
  public Page<Event> findAll(PageDto pageDto) {
    LOGGER.debug("Find all messages");
    return eventRepository.findAll(PageRequest.of(pageDto.pageIndex(), pageDto.pageSize()));
  }

  /**
   * filter returns page object with events matching given criteria.
   *
   * @param eventSearchRequest contains filtering criteria
   * @return page with matching events returned from repo
   */
  @Override
  public Page<Event> filter(EventSearchRequest eventSearchRequest) {
    return eventRepository.findForFilter(eventSearchRequest, PageRequest.of(eventSearchRequest.pageIndex(), eventSearchRequest.pageSize()));
  }

  /**
   * topOfMonth returns page object with events ordered desc after the most sails.
   *
   * @param pageDto holds page infos
   * @return page with events returned from repo
   */
  @Override
  public Page<Event> topOfMonth(PageDto pageDto) {
    return eventRepository.findTopOfMonth(PageRequest.of(pageDto.pageIndex(), pageDto.pageSize()));
  }

  @Override
  public Event create(EventDto event) throws ValidationException {
    //convert GMT to GMT+1
    event.setStartDate(event.getStartDate().plusHours(1));
    event.setEndDate(event.getEndDate().plusHours(1));
    eventValidator.validateEvent(event);
    return eventRepository.save(eventMapper.eventDtoToEvent(event));
  }

  @Override
  public List<String> getCategories() {
    return eventRepository.findCategories();
  }
}

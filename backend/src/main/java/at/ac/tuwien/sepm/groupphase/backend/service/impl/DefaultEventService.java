package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.validator.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultEventService implements EventService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final EventRepository eventRepository;
  private final EventMapper eventMapper;
  private final EventValidator eventValidator;
  private final ArtistMapper artistMapper;
  private final PriceCategoryRepository priceCategoryRepository;

  private final TicketRepository ticketRepository;

  private final RoomRepository roomRepository;
  private final PerformanceRepository performanceRepository;
  private final PricingRepository pricingRepository;

  public DefaultEventService(EventRepository eventRepository, EventMapper eventMapper, EventValidator eventValidator, ArtistMapper artistMapper,
                             PriceCategoryRepository priceCategoryRepository, TicketRepository ticketRepository,
                             RoomRepository roomRepository, PerformanceRepository performanceRepository, PricingRepository pricingRepository) {
    this.eventRepository = eventRepository;
    this.eventMapper = eventMapper;
    this.eventValidator = eventValidator;
    this.artistMapper = artistMapper;
    this.priceCategoryRepository = priceCategoryRepository;
    this.ticketRepository = ticketRepository;
    this.roomRepository = roomRepository;
    this.performanceRepository = performanceRepository;
    this.pricingRepository = pricingRepository;
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
  public Long create(EventCreateDto eventDto) throws ValidationException {
    eventValidator.validateEvent(eventDto);

    Event event = eventMapper.eventDtoToEvent(eventDto);
    eventRepository.save(event);

    List<Ticket> tickets = new ArrayList<>();
    event.setPerformances(eventDto.getPerformances().stream().map(performanceDto -> {
      Performance perf = performanceRepository.save(
        Performance.builder()
          .startDate(performanceDto.getStartDate().plusHours(1))
          .endDate(performanceDto.getEndDate().plusHours(1))
          .artists(artistMapper.artistDtoToArtist(performanceDto.getArtists())).event(event)
          .room(roomRepository.findById(performanceDto.getRoomId())
            .orElseThrow(() -> new NotFoundException("Room with id " + performanceDto.getRoomId() + " could not be found"))).build());

      Map<Long, Pricing> longPricingMap = new HashMap<Long, Pricing>();

      performanceDto.getPriceCategoryPricingMap().forEach((key, value) -> {
        longPricingMap.put(key, Pricing.builder()
          .performance(perf)
          .priceCategory(priceCategoryRepository.findById(key)
            .orElseThrow(() -> new NotFoundException("PriceCategory with id " + key + " could not be found in PriceCategoryPricingMap")))
          .pricing(value)
          .build());
      });

      pricingRepository.saveAll(longPricingMap.values());

      perf.getRoom().getSectors().forEach(sector -> sector.getSeats().forEach(seat -> {
        if (performanceDto.getBlockedSeats().contains(seat.getId())) {
          return;
        }
        tickets.add(
          Ticket.builder()
            .price(longPricingMap.get(sector.getPriceCategory().getId()).getPricing())
            .performance(perf)
            .seat(seat)
            .build()
        );
      }));

      return perf;
    }).collect(Collectors.toSet()));


    ticketRepository.saveAll(tickets);
    Event saved = eventRepository.save(event);
    return saved.getId();
  }

  @Override
  public List<String> getCategories() {
    return eventRepository.findCategories();
  }

  @Override
  public Event getById(long id) throws NotFoundException {
    return this.eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event with id" + id + " could not be found"));
  }
}

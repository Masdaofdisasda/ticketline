package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PageDtoResponse;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.records.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.DateParseException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController()
@RequestMapping(value = "api/v1/events")
public class EventEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final EventService eventService;
  private final EventMapper eventMapper;

  @Autowired
  public EventEndpoint(EventService eventService, EventMapper eventMapper) {
    this.eventService = eventService;
    this.eventMapper = eventMapper;
  }

  @PermitAll
  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(summary = "Get list of events")
  public PageDtoResponse<EventDto> findAll(PageDto pageDto) {
    LOGGER.info("GET /api/v1/events");
    Page<Event> page = eventService.findAll(pageDto);
    return buildResponseDto(pageDto.pageIndex(), pageDto.pageSize(), page.getTotalElements(), page.getTotalPages(),
      eventMapper.eventToEventDto(page.getContent()));
  }

  @PermitAll
  @GetMapping("/filter")
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(summary = "Get list of events")
  public PageDtoResponse<EventDto> filter(EventSearchRequest eventSearchRequest) {
    LOGGER.info("GET /api/v1/events/filter -> query parameters: {}", eventSearchRequest);
    Page<Event> page;
    try {
      page = eventService.filter(eventSearchRequest);
    } catch (InvalidDataAccessApiUsageException exception) {
      throw new DateParseException(exception);
    }

    return buildResponseDto(eventSearchRequest.pageIndex(), page.getSize(), page.getTotalElements(), page.getTotalPages(),
      eventMapper.eventToEventDto(page.getContent()));
  }

  @PermitAll
  @GetMapping("top-of-month")
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(summary = "Get list of events")
  public PageDtoResponse<EventDto> findTopOfMonth(PageDto pageDto) {
    LOGGER.info("GET /api/v1/events/top-of-month");
    Page<Event> page = eventService.topOfMonth(pageDto);
    return buildResponseDto(pageDto.pageIndex(), page.getSize(), page.getTotalElements(), page.getTotalPages(),
      eventMapper.eventToEventDto(page.getContent()));
  }

  @Secured("ROLE_ADMIN")
  @PostMapping("create")
  @ResponseStatus(code = HttpStatus.CREATED)
  @Operation(summary = "Create event", security = @SecurityRequirement(name = "apiKey"))
  public EventDto create(@RequestBody EventDto event) throws ValidationException {
    LOGGER.info("POST /api/v1/events/create");
    return eventMapper.eventToEventDto(eventService.create(event));
  }

  private PageDtoResponse<EventDto> buildResponseDto(int pageIndex, int pageSize, long hits, int pagesTotal, List<EventDto> data) {
    return new PageDtoResponse<>(pageIndex, pageSize, hits, pagesTotal, data);
  }
}
package at.ac.tuwien.sepm.groupphase.backend.service.validator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

@Component
@AllArgsConstructor
public class EventValidator {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private EventRepository eventRepository;

  public void validateEvent(EventCreateDto event) throws ValidationException {
    LOGGER.trace("validateEvent({})", event);

    List<String> validationErrors = new ArrayList<>();

    if (event.getEndDate().isBefore(event.getStartDate())) {
      validationErrors.add("Event start must be earlier than event end.");
    }
    if (eventRepository.findEventsByName(event.getName()).size() > 0) {
      validationErrors.add("Event with the name: '" + event.getName() + "' already exists.");
    }

    if (event.getPerformances() != null && event.getPerformances().size() > 0) {
      event.getPerformances().forEach(performance -> validationErrors.addAll(validatePerformanceOfEvent(performance, event)));

      Map<Long, List<PerformanceCreateDto>> longListMap = event.getPerformances().stream().collect(groupingBy(PerformanceCreateDto::getRoomId));
      longListMap.forEach((key, value) ->
        IntStream.range(0, longListMap.get(key).size()).forEach(idx0 -> {
          PerformanceCreateDto dto0 = longListMap.get(key).get(idx0);
          if (dto0.getStartDate().isBefore(event.getStartDate())) {
            validationErrors.add("Performance #%d starts before events start date".formatted(idx0));
          }
          if (dto0.getEndDate().isAfter(event.getEndDate())) {
            validationErrors.add("Performance #%d ends before events end date".formatted(idx0));
          }
          if (dto0.getStartDate().isAfter(dto0.getEndDate())) {
            validationErrors.add("Performance #%d's end date is before its start date".formatted(idx0));
          }
          IntStream.range(0, value.size()).forEach(idx1 -> {
            if (idx0 == idx1) {
              return;
            }
            PerformanceCreateDto dto1 = value.get(idx1);
            if (dto0.getEndDate().isAfter(dto1.getStartDate())
              && dto0.getStartDate().isBefore(dto1.getEndDate())) {
              validationErrors.add("Performance #%d time-window overlaps with Performance#%d"
                .formatted(idx0, idx1));
            }
          });
        }));
    } else {
      validationErrors.add("Event has to have at least one performance.");
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of Event for create failed.", validationErrors);
    }
  }

  public List<String> validatePerformanceOfEvent(PerformanceCreateDto performance, EventCreateDto event) {
    LOGGER.trace("validatePerformanceOfEvent({}, {})", performance, event);

    List<String> validationErrors = new ArrayList<>();

    if (performance.getStartDate() == null) {
      validationErrors.add("StartDate and time of performance must be set.");
    }
    if (performance.getEndDate() == null) {
      validationErrors.add("EndDate and time of performance must be set.");
    }
    if (performance.getArtists() == null || performance.getArtists().isEmpty()) {
      validationErrors.add("Performing Artist must be set.");
    }
    if (performance.getStartDate().isBefore(event.getStartDate())) {
      validationErrors.add("Performance start date and time must be later or equal to start date and time of event.");
    }
    if (performance.getEndDate().isAfter(event.getEndDate())) {
      validationErrors.add("Performance end date and time must be earlier or equal to end date and time of event.");
    }

    if (performance.getStartDate().isAfter(performance.getEndDate())) {
      validationErrors.add("Performance start must be earlier than performance end.");
    }

    return validationErrors;
  }
}

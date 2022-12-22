package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class EventValidator {


  private EventRepository eventRepository;

  public void validateEvent(EventDto event) throws ValidationException {
    List<String> validationErrors = new ArrayList<>();

    if (event.getEndDate().isBefore(event.getStartDate())) {
      validationErrors.add("Event start must be earlier than event end");
    }
    if (eventRepository.findEventsByName(event.getName()).size() > 0) {
      validationErrors.add("Event with the name: \'" + event.getName() + "\' already exists");
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of user for create failed", validationErrors);
    }
  }
}

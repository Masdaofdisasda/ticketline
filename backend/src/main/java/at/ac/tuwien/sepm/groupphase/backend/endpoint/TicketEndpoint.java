package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketValidationDto;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
public class TicketEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final TicketValidationService ticketValidationService;

  @GetMapping("/validate")
  @PermitAll
  public TicketValidationDto validateTicket(@RequestParam("hash") String validationHash) {
    LOGGER.info("POST /api/v1/ticket/validate: validateTicket({})", validationHash);
    TicketValidationDto validation = new TicketValidationDto(this.ticketValidationService.validateTicket(Base64.getUrlDecoder().decode(validationHash)).toString());
    return validation;
  }
}

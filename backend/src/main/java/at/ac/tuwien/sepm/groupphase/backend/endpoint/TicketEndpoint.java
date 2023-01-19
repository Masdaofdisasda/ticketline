package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.service.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("/validate")
  @PermitAll
  public String validateTicket(@RequestParam("hash") String validationHash) {
    LOGGER.info("POST /api/v1/ticket/validate: validateTicket({})", validationHash);
    return this.ticketValidationService.validateTicket(Base64.getDecoder().decode(validationHash)).toString();
  }

}

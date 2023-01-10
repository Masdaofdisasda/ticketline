package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("/api/v1/performance")
public class PerformanceEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final PerformanceService service;
  private final PerformanceMapper mapper;

  public PerformanceEndpoint(PerformanceService service, PerformanceMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @PermitAll
  @GetMapping("{id}")
  public PerformanceDto getById(@PathVariable long id) {
    LOGGER.info("getById({})", id);
    return this.mapper.performanceToPerformanceDto(service.getById(id));
  }

}

package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("api/v1/seat")
public class SeatEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final SeatService service;
  private final SeatMapper mapper;

  public SeatEndpoint(SeatService service) {
    this.service = service;
    this.mapper = SeatMapper.INSTANCE;
  }

  /**
   * Add a seat to the persistent storage.
   *
   * @param toAdd The seat that should be added
   * @return the seat that was added
   */
  @PostMapping
  @PermitAll
  public Seat addSeat(SeatAddDto toAdd) {
    LOGGER.info("addSeat({})", toAdd);
    return service.createSeat(this.mapper.seatAddDtoToSeat(toAdd));
  }

}

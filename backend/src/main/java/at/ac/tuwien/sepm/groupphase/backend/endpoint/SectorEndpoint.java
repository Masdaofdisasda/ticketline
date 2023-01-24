package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.service.SectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("api/v1/sector")
public class SectorEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final SectorService service;

  public SectorEndpoint(SectorService service) {
    this.service = service;
  }

  /**
   * Add a {@link Sector} to persistent storage.
   *
   * @param toAdd the data of the {@link Sector} that should be added in the RequestBody
   * @return The added {@link Sector}
   */
  @PostMapping
  @PermitAll
  public Sector addSector(@RequestBody Sector toAdd) {
    LOGGER.info("addSector({})", toAdd);
    return service.createSector(toAdd);
  }
}

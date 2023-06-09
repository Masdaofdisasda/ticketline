package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/venue")
public class VenueEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final VenueService service;
  private final VenueMapper mapper;

  public VenueEndpoint(VenueService service) {
    this.mapper = VenueMapper.INSTANCE;
    this.service = service;
  }

  /**
   * Adds a location to persistence.
   *
   * @param toAdd The location to add
   * @return The location that was added
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  @Secured("ROLE_ADMIN")
  @Transactional
  public VenueDto addVenue(@RequestBody VenueAddDto toAdd) throws ValidationException {
    LOGGER.info("addVenue({})", toAdd);

    return this.mapper.venueToVenueDto(
      service.addVenue(
        this.mapper.venueAddDtoToVenue(toAdd)
      )
    );
  }

  /**
   * Delete a location from persistence.
   *
   * @param id The id of the location to delete
   */
  @DeleteMapping("{id}")
  @Secured("ROLE_ADMIN")
  public void deleteVenue(@PathVariable long id) throws NotFoundException {
    LOGGER.info("deleteVenue({})", id);
    service.deleteVenue(id);
  }

  /**
   * Edit a location in persistence.
   *
   * @param id     the id of the location to edit
   * @param toEdit the data the location should be edited by
   * @return the location that was edited
   */
  @PatchMapping("{id}")
  @Secured("ROLE_ADMIN")
  @Transactional
  public VenueDto editVenue(@PathVariable long id, @RequestBody VenueEditDto toEdit) throws NotFoundException, ValidationException {
    LOGGER.info("editVenue({},{})", id, toEdit);
    return mapper.simpleVenueToVenueDto(service.editVenue(id, toEdit));
  }

  /**
   * Get all location stored in persistence.
   *
   * @return A list of all locations
   */
  @GetMapping()
  @PermitAll
  @Transactional
  public Stream<VenueDto> getAllVenues() {
    LOGGER.info("getAllVenues()");
    return service.getAllVenues().stream()
      .map(mapper::simpleVenueToVenueDto);
  }

  /**
   * Get a single location by its id.
   *
   * @param id The id of the location to query
   * @return The location with the given id
   */
  @GetMapping("{id}")
  @PermitAll
  public VenueDto getById(@PathVariable() long id) {
    LOGGER.info("getById({})", id);
    return mapper.venueToVenueDto(service.getById(id));
  }
}

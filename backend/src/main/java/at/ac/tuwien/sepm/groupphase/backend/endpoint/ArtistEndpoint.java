package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@RestController()
@RequestMapping(value = "api/v1/artists")
public class ArtistEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ArtistService artistService;
  private final ArtistMapper artistMapper;
  private final ArtistRepository artistRepository;

  @Autowired
  public ArtistEndpoint(ArtistService artistService, ArtistMapper artistMapper,
                        ArtistRepository artistRepository) {
    this.artistService = artistService;
    this.artistMapper = artistMapper;
    this.artistRepository = artistRepository;
  }

  @PermitAll
  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(summary = "Get list of artists")
  public Stream<ArtistDto> findAll() {
    LOGGER.info("GET /api/v1/artists");
    return artistService.findAll();
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("filter")
  @ResponseStatus(code = HttpStatus.OK)
  @Operation(summary = "get list of artists starting with name of given artist")
  public Stream<ArtistDto> filterByName(@RequestParam String name) {
    LOGGER.info("GetfilterByName /api/v1/artists");
    return artistService.filterByName(new ArtistDto(null, name));
  }


  @Secured("ROLE_ADMIN")
  @PostMapping("create")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create artist", security = @SecurityRequirement(name = "apiKey"))
  public ArtistDto create(@RequestBody ArtistDto artist) throws ValidationException {
    LOGGER.info("POST /api/v1/artists/create");
    return artistMapper.artistToArtistDto(artistService.create(artist));
  }
}

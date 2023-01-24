package at.ac.tuwien.sepm.groupphase.backend.service.validator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ArtistValidator {
  private ArtistRepository artistRepository;

  private static final Logger LOGGER =
    LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void validateArtist(ArtistDto artist) throws ValidationException {
    LOGGER.trace("validateArtist({})", artist);

    List<String> validationErrors = new ArrayList<>();

    if (artistRepository.findArtistsByName(artist.getName()).size() > 0) {
      validationErrors.add("An Artist with the name \'" + artist.getName() + "\' already exists.");
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of Artist for create failed.", validationErrors);
    }
  }
}

package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

public interface PerformanceService {

  /**
   * saves Performance to database.
   *
   * @param performance to be created
   * @return created event
   * @throws ValidationException when validation for PerformanceDto fails
   */
  Performance create(PerformanceDto performance) throws ValidationException;

  /**
   * get a performance from persistence by its id.
   *
   * @param id The id of the performance
   * @return The performance with id = id
   */
  Performance getById(long id) throws NotFoundException;
}

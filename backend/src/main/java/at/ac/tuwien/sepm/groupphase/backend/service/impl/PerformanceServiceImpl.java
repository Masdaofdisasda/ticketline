package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PerformanceMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
public class PerformanceServiceImpl implements PerformanceService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final PerformanceMapper performanceMapper;

  private final PerformanceRepository performanceRepository;

  public PerformanceServiceImpl(PerformanceMapper performanceMapper, PerformanceRepository performanceRepository) {
    this.performanceMapper = performanceMapper;
    this.performanceRepository = performanceRepository;
  }

  @Override
  public Performance create(PerformanceDto performance) {
    return performanceRepository.save(performanceMapper.performanceDtoToPerformance(performance));
  }

  @Override
  public Performance getById(long id) throws NotFoundException {
    LOGGER.trace("getById({})", id);
    Optional<Performance> performanceOptional = performanceRepository.findById(id);
    return performanceOptional.orElseThrow(() -> new NotFoundException("Performance with id " + id + " could not be found"));
  }
}

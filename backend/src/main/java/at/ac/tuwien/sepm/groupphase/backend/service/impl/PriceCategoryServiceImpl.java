package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PriceCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceCategoryServiceImpl implements PriceCategoryService {

  private final PriceCategoryRepository repository;

  private final PerformanceRepository performanceRepository;

  public PriceCategoryServiceImpl(PriceCategoryRepository repository, PerformanceRepository performanceRepository) {
    this.repository = repository;
    this.performanceRepository = performanceRepository;
  }

  @Override
  public PriceCategory createPriceCategory(PriceCategory toAdd) {
    return this.repository.save(toAdd);
  }

  @Override
  public PriceCategory getById(long id) throws NotFoundException {
    Optional<PriceCategory> priceCategoryOptional = this.repository.findById(id);
    if (priceCategoryOptional.isEmpty()) {
      throw new NotFoundException("No PriceCategory with id " + id + " in persistence");
    }
    return priceCategoryOptional.get();
  }

  @Override
  public List<PriceCategory> getAllPriceCategories() {
    return this.repository.findAll();
  }

  @Override
  public List<PriceCategory> getByPerformanceId(long id) {
    return repository.getPriceCategoriesByPerformance(id);
  }

  @Override
  public List<PriceCategory> getByRoomId(long id) {
    return this.repository.getPriceCategoriesByRoom(id);
  }
}

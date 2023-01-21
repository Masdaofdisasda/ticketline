package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriceCategoryRepository extends JpaRepository<PriceCategory, Long> {

  @Query("FROM PriceCategory p WHERE :performance in p.performances")
  List<PriceCategory> getPriceCategoriesByPerformance(@Param("performance") Performance performance);
}

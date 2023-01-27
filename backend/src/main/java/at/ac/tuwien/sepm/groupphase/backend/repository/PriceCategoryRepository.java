package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriceCategoryRepository extends JpaRepository<PriceCategory, Long> {

  @Query("FROM PriceCategory pc "
      + " JOIN FETCH Pricing pr ON pc.id = pr.pricecategory.id"
      + " WHERE pr.performance.id = :performanceId")
  List<PriceCategory> getPriceCategoriesByPerformance(@Param("performanceId") long performanceId);

  @Query("SELECT pc FROM PriceCategory pc"
      + " JOIN pc.sectors sector"
      + " JOIN sector.room room"
      + " WHERE room.id = :id")
  List<PriceCategory> getPriceCategoriesByRoom(@Param("id") long id);
}

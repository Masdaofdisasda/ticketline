package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
  @Query("SELECT pr FROM Pricing pr WHERE pr.performance.id = :performanceId and pr.priceCategory.id = :priceCategoryId")
  Pricing getPricingBy(Long performanceId, Long priceCategoryId);
}

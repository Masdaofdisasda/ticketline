package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceCategoryRepository extends JpaRepository<PriceCategory, Long> {
}

package at.ac.tuwien.sepm.groupphase.backend.datagen.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class PriceCategoryFixture {

  public PriceCategory[] getAll() {
    return new PriceCategory[] {
      buildPriceCategoryBillig(),
      buildPriceCategoryMittel(),
      buildPriceCategoryTeuer()
    };
  }

  public PriceCategory buildPriceCategoryBillig() {
    PriceCategory priceCategory = PriceCategory.builder()
      .id(1L)
      .name("Billig")
      .color("20c661")
      .build();
    priceCategory.setPricings(Set.of(
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(32))
        .build()
    ));

    return priceCategory;
  }

  public PriceCategory buildPriceCategoryMittel() {
    PriceCategory priceCategory = PriceCategory.builder()
      .id(2L)
      .name("Mittel")
      .color("4674c4")
      .build();

    priceCategory.setPricings(Set.of(
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(54.5))
        .build()
    ));

    return priceCategory;
  }

  public PriceCategory buildPriceCategoryTeuer() {
    PriceCategory priceCategory = PriceCategory.builder()
      .id(3L)
      .name("Teuer")
      .color("a9cc12")
      .build();

    priceCategory.setPricings(Set.of(
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(89))
        .build()
    ));

    return priceCategory;
  }
}

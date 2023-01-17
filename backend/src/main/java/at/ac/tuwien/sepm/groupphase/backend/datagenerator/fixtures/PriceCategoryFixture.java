package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

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
    priceCategory.setPricingList(List.of(
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(32))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(44))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(24))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(8))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(24.5))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(62.5))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(100))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(40.4))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(200))
        .build(),
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

    priceCategory.setPricingList(List.of(
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(54.5))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(61.5))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)
        .pricing(BigDecimal.valueOf(33))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(12.5))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(28.5))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(89.3))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(120))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(55))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(320))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(44.44))
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

    priceCategory.setPricingList(List.of(
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(89))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(80))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(55))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(25))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(42))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(110))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(190))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(74))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(400))
        .build(),
      Pricing.builder()
        .priceCategory(priceCategory)

        .pricing(BigDecimal.valueOf(60.5))
        .build()
    ));

    return priceCategory;
  }
}

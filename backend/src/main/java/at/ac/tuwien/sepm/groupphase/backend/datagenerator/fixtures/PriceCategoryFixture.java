package at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures;

import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import org.springframework.stereotype.Component;

@Component
public class PriceCategoryFixture {
  public static PriceCategory[] getAll() {
    return new PriceCategory[] {
      buildPriceCategoryBillig(),
      buildPriceCategoryMittel(),
      buildPriceCategoryTeuer()
    };
  }

  public static PriceCategory buildPriceCategoryBillig() {
    return PriceCategory.builder()
      .id(1L)
      .name("Billig")
      .pricing(null)
      .color("20c661")
      .build();
  }

  public static PriceCategory buildPriceCategoryMittel() {
    return PriceCategory.builder()
      .id(2L)
      .name("Mittel")
      .pricing(null)
      .color("4674c4")
      .build();
  }

  public static PriceCategory buildPriceCategoryTeuer() {
    return PriceCategory.builder()
      .id(3L)
      .name("Teuer")
      .pricing(null)
      .color("a9cc12")
      .build();
  }
}

package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PurchaseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Purchase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PurchaseMapperTest {

  PurchaseMapper mapper = PurchaseMapper.INSTANCE;

  Purchase purchase = Purchase.builder()
    .id(0L)
    .build();

  PurchaseDto purchaseDto = PurchaseDto.builder()
    .id(1L)
    .build();

  @Test
  void purchaseDtoToPurchase() {
    Purchase currentPurchase = mapper.purchaseDtoToPurchase(purchaseDto);
    assertAll(() -> {
      assertThat(currentPurchase.getId()).isEqualTo(purchaseDto.getId());
    });
  }

  @Test
  void purchaseToPurchaseDto() {
    PurchaseDto currentDto = mapper.purchaseToPurchaseDto(purchase);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(purchase.getId());
    });
  }
}
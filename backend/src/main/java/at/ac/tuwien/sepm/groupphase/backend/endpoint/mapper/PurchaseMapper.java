package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PurchaseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseMapper {
  PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

  Purchase purchaseDtoToPurchase(PurchaseDto purchaseDto);

  PurchaseDto purchaseToPurchaseDto(Purchase purchase);
}

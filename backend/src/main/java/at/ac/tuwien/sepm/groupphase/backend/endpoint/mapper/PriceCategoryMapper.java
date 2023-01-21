package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PriceCategoryAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PriceCategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = PricingMapper.class, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceCategoryMapper {

  // PriceCategoryMapper INSTANCE = Mappers.getMapper(PriceCategoryMapper.class);

  PriceCategory priceCategoryDtoToPriceCategory(PriceCategoryDto priceCategoryDto);

  PriceCategory priceCategoryAddDtoToPriceCategory(PriceCategoryAddDto priceCategoryAddDto);

  @Named("priceCategoryToPriceCategoryDto")
  PriceCategoryDto priceCategoryToPriceCategoryDto(PriceCategory priceCategory);

  @IterableMapping(qualifiedByName = "priceCategoryToPriceCategoryDto")
  List<PriceCategoryDto> priceCategoriesToPriceCategoryDtos(List<PriceCategory> priceCategories);
}

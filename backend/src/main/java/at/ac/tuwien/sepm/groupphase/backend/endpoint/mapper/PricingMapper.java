package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PricingDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PricingMapper {

  @Named("pricingToPricingDto")
  @Mapping(target = "performanceId", source = "performance.id")
  @Mapping(target = "categoryId", source = "priceCategory.id")
  PricingDto pricingToPricingDto(Pricing pricing);

  @IterableMapping(qualifiedByName = "pricingToPricingDto")
  List<PricingDto> pricingToPricingDto(List<Pricing> pricing);
}

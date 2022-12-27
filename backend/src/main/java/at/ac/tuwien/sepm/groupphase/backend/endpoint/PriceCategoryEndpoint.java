package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PriceCategoryAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PriceCategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PriceCategoryMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.PriceCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1/price-category")
public class PriceCategoryEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final PriceCategoryService service;
  private final PriceCategoryMapper mapper;

  public PriceCategoryEndpoint(PriceCategoryService service) {
    this.service = service;
    this.mapper = PriceCategoryMapper.INSTANCE;
  }

  @PostMapping()
  @PermitAll
  public PriceCategoryDto createPriceCategory(@RequestBody PriceCategoryAddDto toAdd) {
    LOGGER.info("createPriceCategory({})", toAdd);
    return mapper
      .priceCategoryToPriceCategoryDto(
        service.createPriceCategory(
            mapper.priceCategoryAddDtoToPriceCategory(toAdd)
        )
    );
  }

  @GetMapping()
  @PermitAll
  public Stream<PriceCategoryDto> getAllPriceCategories() {
    LOGGER.info("getAllPriceCategories()");
    return this.service
      .getAllPriceCategories()
      .stream()
      .map(mapper::priceCategoryToPriceCategoryDto);
  }
}

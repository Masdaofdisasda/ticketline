package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PriceCategoryAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PriceCategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PriceCategoryDtoSimple;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PriceCategoryMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.PriceCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1/price-category")
public class PriceCategoryEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final PriceCategoryService service;
  private final PriceCategoryMapper mapper;

  public PriceCategoryEndpoint(PriceCategoryService service, PriceCategoryMapper mapper) {
    this.service = service;
    this.mapper = mapper;
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


  @PermitAll
  @GetMapping("performance/{id}")
  @Transactional(readOnly = true)
  public List<PriceCategoryDto> getPriceCategoriesForPerformance(@PathVariable long id) {
    LOGGER.info("getPriceCategories({})", id);
    return this.mapper.priceCategoriesToPriceCategoryDtos(service.getByPerformanceId(id));
  }


  @PermitAll
  @GetMapping("room/{id}")
  @Transactional(readOnly = true)
  public List<PriceCategoryDtoSimple> getPriceCategoriesForRoom(@PathVariable long id) {
    LOGGER.info("getPriceCategories({})", id);
    return this.mapper.priceCategoriesToPriceCategorySimpleDtos(service.getByRoomId(id));
  }
}

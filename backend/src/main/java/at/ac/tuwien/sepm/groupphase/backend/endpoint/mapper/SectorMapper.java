package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorEditDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {SeatMapper.class, PriceCategoryMapper.class})
public abstract class SectorMapper {

  @Autowired
  SeatMapper seatMapper;

  public abstract SectorDto sectorToSectorDto(Sector sector);

  public abstract Sector sectorDtoToSector(SectorDto sectorDto);

  public abstract Sector sectorEditDtoToSector(SectorEditDto sectorEditDto);

  @Named("sectorToSectorDtoForPerformance")
  public SectorDto sectorToSectorDtoForPerformance(Sector sector, Long performanceId) {
    SectorDto sectorGen = sectorToSectorDto(sector);
    if (performanceId == null) {
      return sectorGen;
    }

    for (Pricing p : sector.getPriceCategory().getPricings()) {
      if (p.getPerformance() != null && Objects.equals(p.getPerformance().getId(), performanceId)) {
        sectorGen.getPriceCategory().setPricing(p.getPricing());
      }
    }

    sectorGen.setSeats(seatMapper.seatsToSeatDtosForPerformance(sector.getSeats(), performanceId));
    return sectorGen;
  }

  @IterableMapping(qualifiedByName = "sectorToSectorDtoForPerformance")
  public List<SectorDto> sectorsToSectorDtosForPerformance(Set<Sector> sectors, Long performanceId) {
    if (sectors == null && performanceId == null) {
      return null;
    }

    ArrayList<SectorDto> arrayList = new ArrayList<SectorDto>();

    for (Sector sector : sectors) {
      arrayList.add(sectorToSectorDtoForPerformance(sector, performanceId));
    }

    return arrayList;
  }
}

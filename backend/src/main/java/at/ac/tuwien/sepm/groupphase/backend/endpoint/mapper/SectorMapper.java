package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SeatMapper.class})
public interface SectorMapper {

  SectorMapper INSTANCE = Mappers.getMapper(SectorMapper.class);

  SectorDto sectorToSectorDto(Sector sector);

  Sector sectorDtoToSector(SectorDto sectorDto);
}

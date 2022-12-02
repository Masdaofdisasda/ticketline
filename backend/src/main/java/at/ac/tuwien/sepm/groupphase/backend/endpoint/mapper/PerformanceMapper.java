package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TicketMapper.class})
public interface PerformanceMapper {

  PerformanceMapper INSTANCE = Mappers.getMapper(PerformanceMapper.class);

  Performance performanceDtoToPerformance(PerformanceDto performanceDto);

  PerformanceDto performanceToPerformanceDto(Performance performance);
}

package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceDtoSimple;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceRoomDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {TicketMapper.class, ArtistMapper.class, RoomMapper.class})
@Transactional(readOnly = true)
public abstract class PerformanceMapper {
  @Autowired
  RoomMapper roomMapper;

  public abstract Performance performanceDtoToPerformance(PerformanceDto performanceDto);

  @Mapping(target = "room",
    expression = "java(roomMapper.roomToRoomDtoForPerformance(performance.getRoom(), performance.getId()))")
  public abstract PerformanceDto performanceToPerformanceDto(Performance performance);

  public abstract PerformanceDtoSimple performanceToperformanceDtoSimple(Performance performance);

  @Mapping(target = "room",
    expression = "java(roomMapper.roomToRoomDtoForPerformance(performance.getRoom(), performance.getId()))")
  public abstract PerformanceRoomDto performanceToPerformanceRoomDto(Performance performance);

  public abstract Performance performanceCreateDtoToPerformance(PerformanceCreateDto dto);
}

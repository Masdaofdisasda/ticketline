package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {RoomMapper.class})
public interface SeatingPlanMapper {
  SeatingPlanMapper INSTANCE = Mappers.getMapper(SeatingPlanMapper.class);

  SeatingPlanDto seatingPlanToSeatingPlanDto(SeatingPlan seatingPlan);

  SeatingPlan seatingPlanDtoToSeatingPlan(SeatingPlanDto seatingPlanDto);
}

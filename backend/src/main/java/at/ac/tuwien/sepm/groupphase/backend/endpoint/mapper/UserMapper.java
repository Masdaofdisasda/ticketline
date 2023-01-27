package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  ApplicationUser userRegistrationDtoToApplicationUser(UserRegistrationDto userDto);

  ApplicationUser userCreationDtoToApplicationUser(UserCreationDto userDto);

  @Named("simpleUser")
  SimpleUserDto userToSimpleUserDto(ApplicationUser user);

  @IterableMapping(qualifiedByName = "simpleUser")
  List<SimpleUserDto> userToSimpleUserDto(List<ApplicationUser> user);
}

package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LocationMapperTest {

  LocationMapper mapper = LocationMapper.INSTANCE;

  Location location = Location.builder()
    .id(0L)
    .city("Vienna")
    .country("Austria")
    .name("Stadthalle")
    .street("Straße")
    .zip(1010)
    .build();

  LocationDto locationDto = LocationDto.builder()
    .id(1L)
    .city("Salzburg")
    .country("Austria")
    .name("Konzerthalle")
    .street("Str")
    .zip(9002)
    .build();

  @Test
  void locationDtoToLocation() {
    Location currentLocation = mapper.locationDtoToLocation(locationDto);

    assertAll(() -> {
      assertThat(currentLocation.getId()).isEqualTo(locationDto.getId());
      assertThat(currentLocation.getCity()).isEqualTo(locationDto.getCity());
      assertThat(currentLocation.getCountry()).isEqualTo(locationDto.getCountry());
      assertThat(currentLocation.getName()).isEqualTo(locationDto.getName());
      assertThat(currentLocation.getStreet()).isEqualTo(locationDto.getStreet());
      assertThat(currentLocation.getZip()).isEqualTo(locationDto.getZip());
    });
  }

  @Test
  void locationToLocationDto() {
    LocationDto currentDto = mapper.locationToLocationDto(location);

    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(location.getId());
      assertThat(currentDto.getCity()).isEqualTo(location.getCity());
      assertThat(currentDto.getCountry()).isEqualTo(location.getCountry());
      assertThat(currentDto.getName()).isEqualTo(location.getName());
      assertThat(currentDto.getStreet()).isEqualTo(location.getStreet());
      assertThat(currentDto.getZip()).isEqualTo(location.getZip());
    });
  }
}
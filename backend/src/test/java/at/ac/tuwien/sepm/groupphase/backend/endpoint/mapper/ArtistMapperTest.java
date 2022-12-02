package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ArtistMapperTest {

  ArtistMapper mapper = ArtistMapper.INSTANCE;

  Artist artist = Artist.builder()
    .id(1L)
    .name("ArtistName")
    .build();

  ArtistDto artistDto = ArtistDto.builder()
    .id(2L)
    .name("ArtistName2")
    .build();

  @Test
  void artistDtoToArtist() {
    Artist currentArtist = mapper.artistDtoToArtist(artistDto);
    assertAll(() -> {
      assertThat(currentArtist.getId()).isEqualTo(artistDto.getId());
      assertThat(currentArtist.getName()).isEqualTo(artistDto.getName());
    });
  }

  @Test
  void testArtistDtoToArtist() {
    ArtistDto currentDto = mapper.artistDtoToArtist(artist);
    assertAll(() -> {
      assertThat(currentDto.getId()).isEqualTo(artist.getId());
      assertThat(currentDto.getName()).isEqualTo(artist.getName());
    });
  }
}
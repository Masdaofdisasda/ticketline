package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ArtistEndpointTest implements TestData {


  @Autowired
  private MockMvc mockMvc;
  @Autowired
  ArtistRepository artistRepository;
  @Autowired
  ArtistMapper artistMapper;

  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  public void createArtist() throws Exception {
    ArtistDto artistDto1 = new ArtistDto(1L, "Artist1");
    ArtistDto artistDto2 = new ArtistDto(2L, "Artist2");

    mockMvc
      .perform(MockMvcRequestBuilders
        .post(ARTIST_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(artistDto1)
        )).andExpect(status().isCreated()).andReturn().getResponse().getContentAsByteArray();

    mockMvc
      .perform(MockMvcRequestBuilders
        .post(ARTIST_BASE_URI + "/create")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(artistDto2)
        )).andExpect(status().isCreated()).andReturn().getResponse().getContentAsByteArray();

    assertThat(artistRepository.findAll().size()).isEqualTo(2);
    artistRepository.deleteAll();
  }

  @Test
  public void findALL() throws Exception {
    artistRepository.save(new Artist(1L, "Artist1", null));
    artistRepository.save(new Artist(2L, "Artist2", null));

    MvcResult mvcResult = this.mockMvc.perform(get(ARTIST_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
      .andDo(print())
      .andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    List<ArtistDto> artistResult = objectMapper.readerFor(ArtistDto.class).<ArtistDto>readValues(response.getContentAsByteArray()).readAll();

    assertEquals(artistResult, List.of(new ArtistDto(1L, "Artist1"), new ArtistDto(2L, "Artist2")));
    artistRepository.deleteAll();
  }

  @Test
  public void filterByName() throws Exception {
    artistRepository.save(new Artist(1L, "Artist1", null));
    artistRepository.save(new Artist(2L, "Artist2", null));
    artistRepository.save(new Artist(3L, "AnotherOne", null));


    MvcResult mvcResult = this.mockMvc.perform(get(ARTIST_BASE_URI + "/filter")
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .param("name", "Art"))
      .andDo(print())
      .andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    List<ArtistDto> artistResult = objectMapper.readerFor(ArtistDto.class).<ArtistDto>readValues(response.getContentAsByteArray()).readAll();

    assertEquals(artistResult.size(), 2);
    assertEquals(artistResult.get(0).getName(), "Artist1");
    assertEquals(artistResult.get(1).getName(), "Artist2");
    artistRepository.deleteAll();
  }
}

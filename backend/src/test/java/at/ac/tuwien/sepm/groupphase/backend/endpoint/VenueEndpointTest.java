package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PriceCategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoomEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueAddDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueEditDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VenueEndpointTest implements TestData {

  final String name = "testName",
    editName = "editName",
    street = "testStreet",
    editStreet = "editStreet",
    houseNumber = "1/1",
    editHouseNumber = "2/2",
    city = "testCity",
    editCity = "editCity",
    zipCode = "01242",
    editZipCode = "1234",
    country = "testCountry",
    editCountry = "editCountry",
    roomName = "testRoom",
    editRoomName = "editRoomName",
    sectorName = "testSector",
    editSectorName = "editSector",
    color = "abcdef",
    editColor = "cdef01",
    priceCategoryName = "testCategory",
    editPriceCategoryName = "editCategory";

  final Integer columnSize = 3,
    editColumnSize = 10,
    rowSize = 3,
    editRowSize = 10,

  columnNumber = 1,
    editColumnNumber = 6,
    rowNumber = 2,
    editRowNumber = 8,
    columnNumber1 = 2,
    editColumnNumber1 = 4,
    rowNumber1 = 2,
    editRowNumber1 = 5;

  final Long locationID = -1L,
    roomID = -1L,
    sectorID = -1L,
    seatID0 = -1L,
    seatID1 = -1L;

  final Pricing pricePerSeat = null;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private VenueRepository venueRepository;

  @Autowired
  private PriceCategoryRepository priceCategoryRepository;

  @Autowired
  private VenueMapper venueMapper;

  @Autowired
  private JwtTokenizer jwtTokenizer;

  @Autowired
  private SecurityProperties securityProperties;

  @Test
  void findAll_shouldReturnEmptyList() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get(VENUE_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
      .andDo(print())
      .andReturn();
    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    List<VenueDto> listResponse = objectMapper.readValue(response.getContentAsString(), List.class);

    assertNotNull(listResponse);
    assertEquals(0, listResponse.size());
  }

  @Test
  void insert_shouldReturnAddedLocation() throws Exception {
    final PriceCategory addedPriceCategory = priceCategoryRepository.save(PriceCategory.builder()
      .color(color)
      .name(priceCategoryName)
      .build());

    final VenueAddDto venue = VenueAddDto.builder()
      .name(name)
      .street(street)
      .houseNumber(houseNumber)
      .city(city)
      .zipCode(zipCode)
      .country(country)
      .rooms(List.of(
        RoomAddDto.builder()
          .columnSize(columnSize)
          .rowSize(rowSize)
          .name(roomName)
          .sectors(List.of(
            SectorAddDto.builder()
              .name(sectorName)
              .priceCategory(PriceCategoryDto.builder()
                .id(addedPriceCategory.getId())
                .color(color)
                .name(priceCategoryName)
                .build())
              .seats(List.of(
                SeatAddDto.builder()
                  .colNumber(columnNumber)
                  .rowNumber(rowNumber)
                  .build()
              ))
              .build()
          )).build()
      ))
      .build();

    MvcResult mvcResult = this.mockMvc.perform(post(VENUE_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .content(Json.pretty(venue))
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andDo(print())
      .andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    VenueDto venueResponse = objectMapper.readValue(response.getContentAsByteArray(), VenueDto.class);

    assertNotNull(venueResponse.getId());
    assertEquals(name, venueResponse.getName());
    assertEquals(street, venueResponse.getStreet());
    assertEquals(houseNumber, venueResponse.getHouseNumber());
    assertEquals(city, venueResponse.getCity());
    assertEquals(zipCode, venueResponse.getZipCode());
    assertEquals(country, venueResponse.getCountry());
    assertEquals(1, venueResponse.getRooms().size());

    final RoomDto room = venueResponse.getRooms().get(0);
    assertEquals(roomName, room.getName());
    assertEquals(columnSize, room.getColumnSize());
    assertEquals(rowSize, room.getRowSize());
    assertEquals(1, room.getSectors().size());

    final SectorDto sector = room.getSectors().get(0);
    assertEquals(sectorName, sector.getName());
    assertEquals(1, sector.getSeats().size());

    final PriceCategoryDto priceCategory = sector.getPriceCategory();
    assertNotNull(priceCategory);
    assertEquals(priceCategoryName, priceCategory.getName());
    assertEquals(color, priceCategory.getColor());

    final SeatDto seat = sector.getSeats().get(0);
    assertEquals(columnNumber, seat.getColNumber());
    assertEquals(rowNumber, seat.getRowNumber());
  }

  @Test
  @Transactional
  void findAll_shouldReturnOneLocation() throws Exception {
    final PriceCategory addedPriceCategory = priceCategoryRepository.save(PriceCategory.builder()
      .color(color)
      .name(priceCategoryName)
      .build());

    final Venue venueToAdd = Venue.builder()
      .id(locationID)
      .name(name)
      .street(street)
      .houseNumber(houseNumber)
      .city(city)
      .zipCode(zipCode)
      .country(country)
      .rooms(Set.of(
        Room.builder()
          .id(roomID)
          .columnSize(columnSize)
          .rowSize(rowSize)
          .name(roomName)
          .sectors(Set.of(
            Sector.builder()
              .id(sectorID)
              .name(sectorName)
              .priceCategory(PriceCategory.builder()
                .id(addedPriceCategory.getId())
                .color(color)
                .name(priceCategoryName)
                .build())
              .seats(Set.of(
                Seat.builder()
                  .id(seatID0)
                  .colNumber(columnNumber)
                  .rowNumber(rowNumber)
                  .build(),
                Seat.builder()
                  .id(seatID1)
                  .colNumber(columnNumber1)
                  .rowNumber(rowNumber1)
                  .build()
              ))
              .build()
          )).build()
      ))
      .build();

    for (Room room : venueToAdd.getRooms()) {
      for (Sector sector : room.getSectors()) {
        for (Seat seat : sector.getSeats()) {
          seat.setSector(sector);
        }
        sector.setRoom(room);
      }
      room.setVenue(venueToAdd);
    }

    venueRepository.save(venueToAdd);

    MvcResult mvcResult = this.mockMvc.perform(get(VENUE_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
      )
      .andDo(print())
      .andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

    List<VenueDto> locationListResponse = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
    });

    // --- Venue ---
    VenueDto venueDto = locationListResponse.get(0);
    assertNotNull(venueDto);
    assertNotNull(venueDto.getId());
    assertEquals(name, venueDto.getName());
    assertEquals(street, venueDto.getStreet());
    assertEquals(houseNumber, venueDto.getHouseNumber());
    assertEquals(city, venueDto.getCity());
    assertEquals(zipCode, venueDto.getZipCode());
    assertEquals(country, venueDto.getCountry());
  }

  @Test
  void editLocation_shouldReturnEdited() throws Exception {
    final PriceCategory addedPriceCategory = priceCategoryRepository.save(PriceCategory.builder()
      .color(color)
      .name(priceCategoryName)
      .build());
    final Venue venueToAdd = at.ac.tuwien.sepm.groupphase.backend.entity.Venue.builder()
      .id(locationID)
      .name(name)
      .street(street)
      .houseNumber(houseNumber)
      .city(city)
      .zipCode(zipCode)
      .country(country)
      .rooms(Set.of(
        Room.builder()
          .id(roomID)
          .columnSize(columnSize)
          .rowSize(rowSize)
          .name(roomName)
          .sectors(Set.of(
            Sector.builder()
              .id(sectorID)
              .name(sectorName)
              .priceCategory(PriceCategory.builder()
                .id(addedPriceCategory.getId())
                .color(color)
                .name(priceCategoryName)
                .build())
              .seats(Set.of(
                Seat.builder()
                  .id(seatID0)
                  .colNumber(columnNumber)
                  .rowNumber(rowNumber)
                  .build(),
                Seat.builder()
                  .id(seatID1)
                  .colNumber(columnNumber1)
                  .rowNumber(rowNumber1)
                  .build()
              ))
              .build()
          )).build()
      ))
      .build();

    for (Room room : venueToAdd.getRooms()) {
      for (Sector sector : room.getSectors()) {
        for (Seat seat : sector.getSeats()) {
          seat.setSector(sector);
        }
        sector.setRoom(room);
      }
      room.setVenue(venueToAdd);
    }

    venueRepository.save(venueToAdd);


    VenueEditDto edit = VenueEditDto.builder()
      .name(editName)
      .city(editCity)
      .street(editStreet)
      .houseNumber(editHouseNumber)
      .zipCode(editZipCode)
      .rooms(List.of(RoomEditDto.builder()
        .columnSize(editColumnSize)
        .rowSize(editRowSize)
        .name(editRoomName)
        .sectors(List.of(SectorEditDto.builder()
          .name(editSectorName)
          .seats(List.of(SeatEditDto.builder()
              .rowNumber(editRowNumber)
              .colNumber(editColumnNumber)
              .build(),
            SeatEditDto.builder()
              .rowNumber(editRowNumber1)
              .colNumber(editColumnNumber1)
              .build()))
          .build()))
        .build()))
      .build();

    MvcResult mvcResult = this.mockMvc.perform(patch(VENUE_BASE_URI)
        .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
        .content(Json.pretty(edit))
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andDo(print())
      .andReturn();
  }
}

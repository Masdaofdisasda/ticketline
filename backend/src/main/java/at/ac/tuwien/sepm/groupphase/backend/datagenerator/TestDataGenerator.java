package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


/*
 * Generell müssen Sie für die Performancetests ein realistisches Set an Testdaten verwenden.
 * Dieses umfasst mindestens 1000 Kunden, 1000 Verkäufe sowie 200 Veranstaltungen an mindestens 25 Orten. (Die Testdaten müssen dabei keine „sinnvollen" Daten sein.)
 * Weitere Anforderungen an Performance sollen Sie selbst ausarbeiten.
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
public class TestDataGenerator implements ApplicationListener<ApplicationReadyEvent> {
  private final DataSource dataSource;

  private final Faker faker = new Faker();
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EventRepository eventRepository;
  private final ArtistRepository artistRepository;
  private final PerformanceRepository performanceRepository;
  private final NewsRepository newsRepository;
  private final VenueRepository venueRepository;
  private final RoomRepository roomRepository;
  private final PriceCategoryRepository priceCategoryRepository;
  private final SectorRepository sectorRepository;
  private final PricingRepository pricingRepository;
  private final SeatRepository seatRepository;
  private final SeatingPlanRepository seatingPlanRepository;
  private final TicketRepository ticketRepository;
  private final BookingRepository bookingRepository;

  /**
   * This event is executed as late as conceivably possible to indicate that
   * the application is ready to service requests.
   */
  @Override
  public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {

    System.out.println("----------------------------------------------------Drop of old data----------------------------------------------------");
    ResourceDatabasePopulator resourceDatabasePopulator =
      new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("sql/create_tables.sql"));
    resourceDatabasePopulator.execute(dataSource);

    System.out.println("------------------------------------------------Starting data generation------------------------------------------------");
    List<ApplicationUser> users = new ArrayList<>();
    List<Artist> artists = new ArrayList<>();
    List<Venue> venues = new ArrayList<>();
    int seatCount = 0;
    List<News> news = new ArrayList<>();

    //admin user generation
    userRepository.save(ApplicationUser.builder()
      .email("admin@email.com")
      .password(passwordEncoder.encode("password"))
      .firstName(faker.name().firstName())
      .lastName(faker.name().lastName())
      .admin(true)
      .failedAttempt(0)
      .accountNonLocked(true)
      .build());

    System.out.println("----------------------------------------------------User generation-----------------------------------------------------");
    //1000 user generation
    for (int i = 0; i < 1000; i++) {
      String userFirstName = faker.superMario().characters();
      String userLastName = faker.name().lastName();
      int failedAttempt = faker.number().numberBetween(0, 10);
      users.add(ApplicationUser.builder()
        .email((userFirstName + userLastName + faker.number().numberBetween(0, 10000) + "@email.com").replace(" ", "."))
        .password(passwordEncoder.encode(faker.lorem().characters(8)))
        .firstName(userFirstName)
        .lastName(userLastName)
        .admin(false)
        .failedAttempt(failedAttempt)
        .accountNonLocked(failedAttempt > 5)
        .lockTime(failedAttempt > 5 ? LocalDateTime.now() : null)
        .build());
    }
    userRepository.saveAll(users);


    System.out.println("----------------------------------------------------PriceCategory generation----------------------------------------------------");


    List<PriceCategory> priceCategories = new ArrayList<>();
    priceCategories.add(priceCategoryRepository.save(PriceCategory.builder()
      .color("00FF00")
      .name("Billig")
      .build()));

    priceCategories.add(priceCategoryRepository.save(PriceCategory.builder()
      .color("0000FF")
      .name("Mittel")
      .build()));

    priceCategories.add(priceCategoryRepository.save(PriceCategory.builder()
      .color("FF0000")
      .name("Teuer")
      .build()));


    System.out.println("----------------------------------------------------Venue generation----------------------------------------------------");
    //25 venues generation
    for (int i = 0; i < 25; i++) {

      artists.add(Artist.builder()
        .name(faker.artist().name())
        .build());

      final String fileName = "news" + i + ".jpg";

      try {
        saveUrlToFile(new URL("https://fastly.picsum.photos/id/393/200/300.jpg?hmac=zh8LVueWlQFz83Gn-9g49laZIMmCg_NC6jLkrQq0h5U"),
          new File(new URL(getClass().getClassLoader().getResource("."), fileName).toURI()));
      } catch (IOException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }

      news.add(News.builder()
        .fileName(faker.file().fileName())
        .title(faker.book().title())
        .summary(faker.yoda().quote())
        .text(String.join("", faker.lorem().sentences(20)))
        .publishedAt(getMapOfRandomStartAndEndDate().get("startDate"))
        .fileName(fileName)
        .build());

      net.datafaker.providers.base.Address address = faker.address();

      Venue venue = venueRepository.save(Venue.builder()
        .name(faker.pokemon().location())
        .city(address.city())
        .zipCode(address.zipCode())
        .country(address.country())
        .street(address.streetName())
        .houseNumber(address.buildingNumber())
        .build());

      List<Room> rooms = new ArrayList<>();
      for (int j = 0; j < faker.number().numberBetween(1, 4); j++) {
        int numb = Math.round((faker.number().numberBetween(10, 50) / 10) * 10);
        Room room = Room.builder()
          .name(faker.pokemon().name())
          .venue(venue)
          .columnSize(numb)
          .rowSize(numb)
          .build();

        room = roomRepository.save(room);
        Set<Sector> sectors = new HashSet<>();
        for (int k = 0; k < 3; k++) {
          Sector sector = sectorRepository.save(Sector.builder()
            .room(room)
            .name(faker.funnyName().name())
            .priceCategory(priceCategories.get(k))
            .build());
          priceCategories.get(k).setSectors(Set.of(sector));
          sectors.add(sector);
        }
        room.setSectors(sectors);

        for (int k = 0; k < room.getRowSize(); k++) {
          for (int l = 0; l < room.getColumnSize(); l++) {
            PriceCategory priceCategory = priceCategories.get(getIndex(l, 3, room.getColumnSize()));
            Seat seat = Seat.builder()
              .rowNumber(k)
              .rowName(String.valueOf(k))
              .colNumber(l)
              .colName(String.valueOf(l))
              .sector(priceCategory.getSectors().stream().toList().get(0))
              .build();
            priceCategory.getSectors().stream().toList().get(0).addSeat(seat);
            seatRepository.save(seat);
            seatCount++;
          }
        }
        sectorRepository.saveAll(room.getSectors());


        SeatingPlan seatingPlan = SeatingPlan.builder()
          .room(room)
          .columns(room.getColumnSize())
          .rows(room.getRowSize())
          .build();
        seatingPlanRepository.save(seatingPlan);

        rooms.add(room);
      }
      roomRepository.saveAll(rooms);
      venue.setRooms(new HashSet<>(rooms));
      venues.add(venue);
    }
    artistRepository.saveAll(artists);
    newsRepository.saveAll(news);

    System.out.println("----------------------------------------------------Event generation----------------------------------------------------");
    for (int i = 0; i <= 200; i++) {

      Map<String, LocalDateTime> dates = getMapOfRandomStartAndEndDate();

      Event event = eventRepository.save(Event.builder()
        .name(faker.funnyName().name())
        .category(faker.music().genre())
        .startDate(dates.get("startDate"))
        .endDate(dates.get("endDate"))
        .build());

      Venue venue = venues.get(faker.number().numberBetween(0, venues.size() - 1));
      Room room = venue.getRooms().stream().toList().get(faker.number().numberBetween(0, venue.getRooms().size() - 1));

      Set<Artist> artistsForPerformance = new HashSet<>();
      ThreadLocalRandom.current().ints(0, artists.size()).distinct().limit(faker.number().numberBetween(1, 4)).forEach(randindex -> {
        artistsForPerformance.add(artists.get(randindex));
      });

      Performance performance = performanceRepository.save(Performance.builder()
        .artists(new HashSet<>(artistsForPerformance))
        .startDate(dates.get("startDate"))
        .endDate(dates.get("endDate"))
        .event(event)
        .room(room)
        .build());

      for (int j = 0; j < 3; j++) {
        Pricing pricing = pricingRepository.save(Pricing.builder()
          .pricing(BigDecimal.valueOf(faker.number().numberBetween(15 * (j + 1), 30 * (j + 1))))
          .performance(performance)
          .priceCategory(priceCategories.get(j))
          .build());

        priceCategories.get(j).setPricings(Set.of(pricing));
      }

      room.getSectors().forEach(sector -> {
        List<Ticket> tickets = new ArrayList<>();
        sector.getSeats().forEach(seat -> {
          Ticket ticket = Ticket.builder()
            .seat(seat)
            .performance(performance)
            .price(sector.getPriceCategory().getPricings().stream().toList().get(0).getPricing())
            .build();
          tickets.add(ticket);
        });
        ticketRepository.saveAll(tickets);
      });
    }

    System.out.println("------------------------------------------------Generate Bookings------------------------------------------------");

    List<Booking> bookings = new ArrayList<>();
    for (int bookingCount = 0; bookingCount < 600; bookingCount++) {
      ApplicationUser user = users.get(faker.number().numberBetween(0, users.size() - 1));
      Set<Ticket> ticketsToBook = new HashSet<>();
      Booking booking = Booking.builder()
        .user(user)
        .createdDate(LocalDateTime.now())
        .bookingType(BookingType.PURCHASE)
        .build();
      bookingRepository.save(booking);
      ThreadLocalRandom.current().ints(1, seatCount - 1).distinct().limit(4).forEach(index -> {
        Ticket ticket = ticketRepository.findById((long) index).get();
        ticket.setBooking(booking);
        ticketsToBook.add(ticket);
      });
      booking.setTickets(ticketsToBook);
      ticketRepository.saveAll(ticketsToBook);
      bookings.add(booking);
      System.out.println("endloop");
    }
    System.out.println("saving now");
    bookingRepository.saveAll(bookings);

    System.out.println("------------------------------------------------Finished data generation------------------------------------------------");
  }

  Map<String, LocalDateTime> getMapOfRandomStartAndEndDate() {
    java.util.Date dateStart = faker.date().between(
      Date.valueOf(LocalDate.of(2020, 1, 1)),
      Date.valueOf(LocalDate.of(2025, 1, 1)));

    LocalDateTime startDate = LocalDateTime.parse(dateStart.toString().replace(" ", "T"));

    java.util.Date dateEnd = faker.date().between(
      dateStart,
      new java.util.Date(dateStart.getTime() + 86400000));

    LocalDateTime endDate = LocalDateTime.parse(dateEnd.toString().replace(" ", "T"));

    return Map.of("startDate", startDate, "endDate", endDate);
  }

  //gibt den index(welches drittel, viertel etc.) zurück wo current grad steht relativ zu max gesehen
  int getIndex(int current, int divider, int max) {
    double piece = (double) max / (double) divider;
    for (int i = 1; i <= divider; i++) {
      if (current <= i * piece) {
        return i - 1;
      }
    }
    System.out.println("Out of Bound for current:" + current + " divider:" + divider + " max:" + max);
    return 0;
  }

  private void saveUrlToFile(URL url, File file) throws IOException {
    FileUtils.copyURLToFile(url, file);
  }
}
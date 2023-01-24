package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.PriceCategoryFixture;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.SectorFixture;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Pricing;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PriceCategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.ArtistFixture.getBuildArtist;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.EventFixture.getBuildEvent;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.RoomFixture.getBuildRoom;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.VenueFixture.getBuildVenue;

@Profile("generateData")
@Component
@DependsOn("userDataGenerator")
public class PerformanceDataGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final int NUMBER_OF_PERFORMANCES_TO_GENERATE = 10;
  final PriceCategoryFixture priceCategoryFixture;
  private final PerformanceRepository performanceRepository;
  private final EventRepository eventRepository;
  private final ArtistRepository artistRepository;
  private final VenueRepository venueRepository;
  private final TicketRepository ticketRepository;
  private final BookingRepository bookingRepository;
  private final RoomRepository roomRepository;

  private final UserRepository userRepository;

  private final PricingRepository pricingRepository;

  private final PriceCategoryRepository priceCategoryRepository;

  public PerformanceDataGenerator(PerformanceRepository performanceRepository,
                                  EventRepository eventRepository,
                                  ArtistRepository artistRepository,
                                  VenueRepository venueRepository,
                                  TicketRepository ticketRepository,
                                  BookingRepository bookingRepository,
                                  RoomRepository roomRepository, PriceCategoryRepository priceCategoryRepository, PriceCategoryFixture priceCategoryFixture, UserRepository userRepository, PricingRepository pricingRepository) {
    this.performanceRepository = performanceRepository;
    this.eventRepository = eventRepository;
    this.artistRepository = artistRepository;
    this.venueRepository = venueRepository;
    this.ticketRepository = ticketRepository;
    this.bookingRepository = bookingRepository;
    this.roomRepository = roomRepository;
    this.priceCategoryRepository = priceCategoryRepository;
    this.priceCategoryFixture = priceCategoryFixture;
    this.userRepository = userRepository;
    this.pricingRepository = pricingRepository;
  }

  @PostConstruct
  public void generatePerformance() {
    if (performanceRepository.findAll().size() > 0) {
      LOGGER.debug("performance already generated");
    } else {
      LOGGER.debug("generating {} performance entries", NUMBER_OF_PERFORMANCES_TO_GENERATE);

      priceCategoryRepository.saveAll(Arrays.asList(priceCategoryFixture.getAll()));
      // This is painful but the only way it really works, this will be changed anyway when the data model for PriceCategory is adjusted
      SectorFixture.repository = priceCategoryRepository;

      for (int i = 1; i <= NUMBER_OF_PERFORMANCES_TO_GENERATE; i++) {
        venueRepository.save(getBuildVenue(i));
        Artist artist = artistRepository.save(getBuildArtist(i));
        Event event = eventRepository.save(getBuildEvent(i));
        Room room = roomRepository.save(getBuildRoom(i));

        Performance performance = Performance.builder()
          .id((long) i)
          .startDate(LocalDateTime.now().plusDays(i))
          .endDate(LocalDateTime.now().plusDays(i + 7))
          .room(room)
          .artists(List.of(artist))
          .event(event)
          .build();

        performance = performanceRepository.save(performance);

        int finalI = i;
        Performance finalPerformance = performance;
        List<Pricing> pricings = new ArrayList<>();
        priceCategoryRepository.findAll().forEach(pc -> {
          Pricing pricing = pc.getPricingList().get(finalI - 1);
          pricing.setPerformance(finalPerformance);
          pricings.add(pricing);
        });


        pricingRepository.saveAll(pricings);

        List<Ticket> tickets = new ArrayList<>();

        for (Sector sector : performance.getRoom().getSectors()) {
          for (Seat seat : sector.getSeats()) {
            Ticket ticket = Ticket.builder()
              .price(pricings.stream().filter(pricing -> pricing.getPerformance().getId()
                  .equals(finalPerformance.getId())).findFirst().orElseThrow().getPricing())
              .performance(performance)
              .seat(seat)
              .build();

            if (Math.random() > .8) {
              ticket.setBooking(Booking.builder()
                .tickets(List.of(ticket))
                .createdDate(LocalDateTime.now())
                .user(userRepository.findAll().get((int) (Math.random() * 3)))
                .bookingType(BookingType.values()[(int) (Math.random() * 2)])
                .build());
            }

            tickets.add(ticket);
          }
        }

        ticketRepository.saveAll(tickets);


        performance.setTickets(tickets);

        LOGGER.debug("saving performance {}", performance);
        performanceRepository.save(performance);
      }
    }
  }
}

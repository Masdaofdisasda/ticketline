package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Room;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Venue;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoomRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.ArtistFixture.getBuildArtist;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.EventFixture.getBuildEvent;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.RoomFixture.getBuildRoom;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.TicketFixture.getBuildTickets;
import static at.ac.tuwien.sepm.groupphase.backend.datagenerator.fixtures.VenueFixture.getBuildVenue;

@Profile("generateData")
@Component
public class PerformanceDataGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final int NUMBER_OF_PERFORMANCES_TO_GENERATE = 10;

  private final PerformanceRepository performanceRepository;
  private final EventRepository eventRepository;
  private final ArtistRepository artistRepository;
  private final VenueRepository venueRepository;
  private final TicketRepository ticketRepository;
  private final BookingRepository bookingRepository;
  private final RoomRepository roomRepository;

  public PerformanceDataGenerator(PerformanceRepository performanceRepository,
                                  EventRepository eventRepository,
                                  ArtistRepository artistRepository,
                                  VenueRepository venueRepository,
                                  TicketRepository ticketRepository,
                                  BookingRepository bookingRepository,
                                  RoomRepository roomRepository) {
    this.performanceRepository = performanceRepository;
    this.eventRepository = eventRepository;
    this.artistRepository = artistRepository;
    this.venueRepository = venueRepository;
    this.ticketRepository = ticketRepository;
    this.bookingRepository = bookingRepository;
    this.roomRepository = roomRepository;
  }

  @PostConstruct
  private void generatePerformance() {
    if (performanceRepository.findAll().size() > 0) {
      LOGGER.debug("performance already generated");
    } else {
      LOGGER.debug("generating {} performance entries", NUMBER_OF_PERFORMANCES_TO_GENERATE);
      for (int i = 1; i <= NUMBER_OF_PERFORMANCES_TO_GENERATE; i++) {

        Venue venue = venueRepository.save(getBuildVenue(i));
        Artist artist = artistRepository.save(getBuildArtist(i));
        Event event = eventRepository.save(getBuildEvent(i));
        List<Ticket> tickets = getBuildTickets(i);
        Room room = roomRepository.save(getBuildRoom(i));

        Performance performance = Performance.builder()
          .id((long) i)
          .startDate(LocalDateTime.now().plusDays(i))
          .endDate(LocalDateTime.now().plusDays(i + 7))
          .room(room)
          .artist(artist)
          .event(event)
          .build();

        performanceRepository.save(performance);

        for (Ticket ticket : tickets) {
          ticket = ticket.toBuilder()
            .booking(bookingRepository.save(Booking.builder()
              .id((long) i)
              .bookingType(BookingType.PURCHASE)
              .tickets(List.of(ticket))
              .createdDate(LocalDateTime.now().minusDays(i))
              .build()
            )).performance(performance).build();

          ticketRepository.save(ticket);
        }

        performance.setTickets(tickets);

        LOGGER.debug("saving performance {}", performance);
        performanceRepository.save(performance);
      }
    }
  }
}

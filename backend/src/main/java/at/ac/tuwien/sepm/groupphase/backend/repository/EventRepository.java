package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchRequest;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  // TODO: 23.12.22 future datetimes where no event takes place do throw an exception

  /**
   * findForFilter returns a page object containing all or a slice of events matching given criteria.
   *
   * @param eventSearchRequest holds parameters to check against
   * @param pageable           holds page information like index and pagesize
   * @return page with matching events
   */
  @Query(
    value = "SELECT ev FROM Event ev"
      + " LEFT JOIN Performance pf ON pf.id IN ELEMENTS(ev.performances)"
      + " LEFT JOIN Artist art ON pf.artist = art"
      + " LEFT JOIN Room r ON pf.room = r"
      + " LEFT JOIN Venue v ON r.venue = v"
      + " WHERE((:#{#eventSearchRequest.startTime} IS NOT NULL AND ev.startDate >= :#{#eventSearchRequest.startTime})"
      + " OR (:#{#eventSearchRequest.startTime} IS NULL AND ev.startDate >= CURRENT_TIMESTAMP)"
      + " AND (coalesce(:#{#eventSearchRequest.endTime},'')='' OR ev.endDate <= :#{#eventSearchRequest.endTime}))"
      + " AND (coalesce(:#{#eventSearchRequest.nameOfEvent}, '')='' OR lower(ev.name) like concat('%',lower(:#{#eventSearchRequest.nameOfEvent}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.venueName},'')='' OR lower(v.name) like concat('%',lower(:#{#eventSearchRequest.venueName}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.eventHall},'')='' OR lower(r.name) like concat('%',lower(:#{#eventSearchRequest.eventHall}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.zipCode},'')='' OR v.zipCode = :#{#eventSearchRequest.zipCode})"
      + " AND (coalesce(:#{#eventSearchRequest.country},'')='' OR lower(v.country) like concat('%',lower(:#{#eventSearchRequest.country}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.city},'')='' OR lower(v.city) like concat('%',lower(:#{#eventSearchRequest.city}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.street},'')='' OR lower(v.street) like concat('%',lower(:#{#eventSearchRequest.street}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.artistName},'')='' OR lower(art.name) like concat('%',lower(:#{#eventSearchRequest.artistName}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.category},'')='' OR lower(ev.category) like concat('%',lower(:#{#eventSearchRequest.category}),'%'))")
  Page<Event> findForFilter(@Param("eventSearchRequest") EventSearchRequest eventSearchRequest, Pageable pageable);

  /**
   * findTopOfMonth return events with the most sails ordered desc.
   *
   * @param pageable holds page information like index and pagesize
   * @return page with matching events
   */
  @Query("SELECT ev FROM Event ev"
    + " LEFT JOIN Performance pf ON pf.event = ev"
    + " LEFT JOIN Ticket tk ON tk.performance = pf"
    + " WHERE tk.booking IS NOT NULL"
    + " AND Month(ev.startDate) = Month(CURRENT_DATE)"
    + " GROUP BY ev ORDER BY count(tk.booking) desc")
  Page<Event> findTopOfMonth(Pageable pageable);

  /**
   * Get event categories saved in the db.
   *
   * @return event categories
   */
  @Query("SELECT DISTINCT ev.category FROM Event ev")
  List<String> findCategories();

  List<Event> findEventsByName(String name);
}
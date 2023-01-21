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
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  /**
   * findForFilter returns a page object containing all or a slice of events matching given criteria.
   *
   * @param eventSearchRequest holds parameters to check against
   * @param pageable           holds page information like index and pagesize
   * @return page with matching events
   */
  @Query(
    value = "SELECT DISTINCT ev FROM Event ev"
      + " LEFT JOIN Performance pf ON pf.id IN ELEMENTS(ev.performances)"
      + " LEFT JOIN Artist art ON art IN ELEMENTS(pf.artists)"
      + " LEFT JOIN Room r ON pf.room = r"
      + " LEFT JOIN Venue v ON r.venue = v"
      + " WHERE(((:#{#eventSearchRequest.startTime} IS NOT NULL AND ev.startDate >= :#{#eventSearchRequest.startTime})"
      + " OR (:#{#eventSearchRequest.startTime} IS NULL AND ev.startDate >= CURRENT_TIMESTAMP)"
      + " AND (coalesce(:#{#eventSearchRequest.endTime},'')='' OR ev.endDate <= :#{#eventSearchRequest.endTime})))"
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
  @Query("select pf.event FROM Performance pf"
    + " LEFT join Ticket t ON t.performance = pf"
    + " AND Month(pf.event.startDate) = Month(CURRENT_DATE)"
    + " GROUP BY pf.event"
    + " ORDER BY COUNT(distinct(COALESCE(t.booking, 0))) desc")
  Page<Event> findTopOfMonth(Pageable pageable);


  /**
   * Get event categories saved in the db.
   *
   * @return event categories
   */
  @Query("SELECT DISTINCT ev.category FROM Event ev")
  List<String> findCategories();

  List<Event> findEventsByName(String name);

  @Query("SELECT ev FROM Event ev"
    + " LEFT JOIN FETCH Performance pf ON pf.id IN ELEMENTS(ev.performances)"
    + " LEFT JOIN FETCH Artist art ON art IN ELEMENTS(pf.artists)"
    + " LEFT JOIN FETCH Room r ON pf.room = r"
    + " LEFT JOIN FETCH Sector s ON s IN ELEMENTS(r.sectors)"
    + " LEFT JOIN FETCH Venue v ON r.venue = v"
    + " WHERE ev.id = :id")
  Optional<Event> getById(@Param("id") long id);
}
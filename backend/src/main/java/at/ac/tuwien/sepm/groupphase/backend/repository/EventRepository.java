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

  // TODO: 11.12.22 date selection in frontend to comparing against database entries does not work, because different formats maybe some one else can fix this

  /**
   * findForFilter returns a page object containing all or a slice of events matching given criteria.
   *
   * @param eventSearchRequest holds parameters to check against
   * @param pageable           holds page information like index and pagesize
   * @return page with matching events
   */
  @Query(
    value = "select ev from Event ev"
      + " LEFT JOIN Performance pf ON pf.id IN elements(ev.performances)"
      + " LEFT JOIN Artist art ON pf.artist = art"
      + " LEFT JOIN Location loc ON pf.location = loc"
      + " WHERE (coalesce(:#{#eventSearchRequest.startTime},'')='' OR ev.startDate >= :#{#eventSearchRequest.startTime})"
      + " AND (coalesce(:#{#eventSearchRequest.nameOfEvent}, '')='' OR lower(ev.name) like concat('%',lower(:#{#eventSearchRequest.nameOfEvent}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.eventHall},'')='' OR lower(loc.name) like concat('%',lower(:#{#eventSearchRequest.eventHall}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.zipCode},'')='' OR loc.zip = :#{#eventSearchRequest.zipCode})"
      + " AND (coalesce(:#{#eventSearchRequest.country},'')='' OR lower(loc.country) like concat('%',lower(:#{#eventSearchRequest.country}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.city},'')='' OR lower(loc.city) like concat('%',lower(:#{#eventSearchRequest.city}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.street},'')='' OR lower(loc.street) like concat('%',lower(:#{#eventSearchRequest.street}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.artistName},'')='' OR lower(art.name) like concat('%',lower(:#{#eventSearchRequest.artistName}),'%'))"
      + " AND (coalesce(:#{#eventSearchRequest.genre},'')='' OR lower(ev.category) like concat('%',lower(:#{#eventSearchRequest.genre}),'%'))")
  Page<Event> findForFilter(@Param("eventSearchRequest") EventSearchRequest eventSearchRequest, Pageable pageable);

  /**
   * findTopOfMonth return events with the most sails ordered desc.
   *
   * @param pageable holds page information like index and pagesize
   * @return page with matching events
   */
  @Query("select ev FROM Event ev, Performance pf, Ticket tk"
    + " WHERE pf.event = ev"
    + " AND tk.performance = pf"
    + " AND tk.booking is not null"
    + " GROUP by ev"
    + " ORDER BY count(tk.booking) desc")
  Page<Event> findTopOfMonth(Pageable pageable);

  List<Event> findEventsByName(String name);
}
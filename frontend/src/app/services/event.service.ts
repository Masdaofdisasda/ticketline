import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {EventDto} from '../dto/event.dto';
import {EventSearchRequest} from '../dto/event-search-request';
import {PageResponseDto} from '../dto/page-response.dto';
import {PageDto} from '../dto/page.dto';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventBaseUri = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /*
   * Load all events from the backend
   */
  getEvents(): Observable<PageResponseDto<EventDto>> {
    const params = new HttpParams()
      .set('pageIndex', 0)
      .set('pageSize', 10);

    return this.httpClient.get<PageResponseDto<EventDto>>(this.eventBaseUri, {params});
  }

  filter(eventSearchRequest?: EventSearchRequest): Observable<PageResponseDto<EventDto>> {
    const params = new HttpParams()
      .set('artistName', eventSearchRequest.artistName)
      .set('country', eventSearchRequest.country)
      .set('city', eventSearchRequest.city)
      .set('street', eventSearchRequest.street)
      .set('zipCode', eventSearchRequest.zipCode)
      .set('venueName', eventSearchRequest.venueName)
      .set('eventHall', eventSearchRequest.eventHall)
      .set('from', eventSearchRequest.from)
      .set('startTime', eventSearchRequest.startTime)
      .set('genre', eventSearchRequest.genre)
      .set('nameOfEvent', eventSearchRequest.nameOfEvent)
      .set('pageIndex', eventSearchRequest.pageIndex)
      .set('pageSize', eventSearchRequest.pageSize);

    return this.httpClient.get<PageResponseDto<EventDto>>(this.eventBaseUri + '/filter', {params});
  }

  getTopEventsOfMonth(pageDto: PageDto): Observable<PageResponseDto<EventDto>> {
    const params = new HttpParams()
      .set('pageIndex', pageDto.pageIndex)
      .set('pageSize', pageDto.pageSize);

    return this.httpClient.get<PageResponseDto<EventDto>>(this.eventBaseUri + '/top-of-month', {params});
  }

  create(event: EventDto): Observable<EventDto> {
    return this.httpClient.post<EventDto>(this.eventBaseUri + '/create', event);
  }
}

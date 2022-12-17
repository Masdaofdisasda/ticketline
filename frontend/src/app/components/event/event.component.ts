import {Component} from '@angular/core';
import {EventService} from '../../services/event.service';
import {EventSearchRequest} from '../../dto/event-search-request';
import {PageDto} from '../../dto/page.dto';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent {
  lastRequest = EventSearchRequest.getEmptyRequest();
  pagedEventDtos = this.eventService.getEvents();
  eventsTopOfTheMonth = this.eventService.getTopEventsOfMonth(PageDto.initialPage());

  constructor(private eventService: EventService) {
  }

  search($event: EventSearchRequest) {
    //search field should not interfer with pagination
    this.lastRequest = {
      ...$event,
      pageIndex: this.lastRequest.pageIndex,
      pageSize: this.lastRequest.pageSize
    };

    this.pagedEventDtos = this.eventService.filter(this.lastRequest);
  }

  sendPagedEventRequest($event: PageDto) {
    this.lastRequest = {
      ...this.lastRequest,
      pageIndex: $event.pageIndex,
      pageSize: $event.pageSize
    };

    this.pagedEventDtos = this.eventService.filter(this.lastRequest);
  }

  sendPagedTopOfTheMonthEventRequest($event: PageDto) {
    this.eventsTopOfTheMonth = this.eventService.getTopEventsOfMonth($event);
  }

}

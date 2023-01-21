import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {PageResponseDto} from '../../../dto/page-response.dto';
import {PageDto} from '../../../dto/page.dto';
import {ExtendedEventDto} from '../../../dto/extended-event.dto';
import {Router} from '@angular/router';
import {EventDto} from '../../../dto/event.dto';

@Component({
  selector: 'app-event-search-result',
  templateUrl: './event-search-result.component.html',
  styleUrls: ['./event-search-result.component.scss']
})
export class EventSearchResultComponent implements OnChanges {

  @Input() pagedEvents: PageResponseDto<ExtendedEventDto>;
  @Output() pageChangedEvent = new EventEmitter<PageDto>();

  pagedEventsDefault = PageResponseDto.getPageResponseDto();

  constructor(private router: Router) {
  }

  refreshEvents() {
    this.pageChangedEvent.emit({
      pageSize: this.pagedEventsDefault.pageSize,
      pageIndex: this.pagedEventsDefault.pageIndex - 1
    } as PageDto);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.pagedEvents.currentValue) {
      // this is needed because bootstrap pagination and spring pagination have an offset
      changes.pagedEvents.currentValue['pageIndex'] = changes.pagedEvents.currentValue['pageIndex'] + 1;
      this.pagedEventsDefault = changes.pagedEvents.currentValue;
    }
  }

  getTicketsClicked(event: EventDto) {
    if (event.performances.length === 1) {
      this.router.navigateByUrl(`event/performance/${event.performances[0].id}/seats`);
    } else {
      this.router.navigateByUrl(`event/${event.id}/performances`);
    }
  }
}

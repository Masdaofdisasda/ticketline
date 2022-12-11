import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventComponent} from './event.component';
import {EventService} from '../../services/event.service';
import {HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {EventSearchRequest} from '../../dto/event-search-request';
import {PageDto} from '../../dto/page.dto';

describe('EventComponent', () => {
  let component: EventComponent;
  let fixture: ComponentFixture<EventComponent>;
  const eventService = {
    getEvents: jasmine.createSpy('getEvents'),
    filter: jasmine.createSpy('filter'),
    getTopEventsOfMonth: jasmine.createSpy('getTopEventsOfMonth'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientModule, NgbModule],
      providers: [{
        provide: EventService,
        useValue: eventService
      }],
      declarations: [EventComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(EventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(eventService).toBeTruthy();
    expect(component).toBeTruthy();
  });

  it('should call eventService with last used pagination', () => {
    component.lastRequest = {
      pageIndex: 0,
      pageSize: 2
    } as EventSearchRequest;


    component.search({
      artistName: '',
      country: '',
      city: '',
      street: '',
      zipCode: '',
      venueName: '',
      eventHall: '',
      from: '',
      startTime: '',
      genre: '',
      nameOfEvent: '',
      pageIndex: 0,
      pageSize: 10
    });

    expect(eventService.filter).toHaveBeenCalledWith({
      artistName: '',
      country: '',
      city: '',
      street: '',
      zipCode: '',
      venueName: '',
      eventHall: '',
      from: '',
      startTime: '',
      genre: '',
      nameOfEvent: '',
      pageIndex: 0,
      pageSize: 2
    } as EventSearchRequest);
  });

  it('should set lastRequest to new pagination values and call eventService with new pagination', () => {
    component.lastRequest = {
      artistName: '',
      country: '',
      city: '',
      street: '',
      zipCode: '',
      venueName: '',
      eventHall: '',
      from: '',
      startTime: '',
      genre: 'Genre',
      nameOfEvent: '',
      pageIndex: 1,
      pageSize: 10
    } as EventSearchRequest;

    component.sendPagedEventRequest({
      pageIndex: 0,
      pageSize: 10
    } as PageDto);

    expect(eventService.filter).toHaveBeenCalledWith({
      artistName: '',
      country: '',
      city: '',
      street: '',
      zipCode: '',
      venueName: '',
      eventHall: '',
      from: '',
      startTime: '',
      genre: 'Genre',
      nameOfEvent: '',
      pageIndex: 0,
      pageSize: 10
    } as EventSearchRequest);
  });
});

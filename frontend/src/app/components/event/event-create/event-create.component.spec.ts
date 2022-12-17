import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventCreateComponent} from './event-create.component';
import {EventService} from '../../../services/event.service';
import {NgbDate, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {NgbTime} from '@ng-bootstrap/ng-bootstrap/timepicker/ngb-time';
import {EventDto} from '../../../dto/event.dto';

describe('EventCreateComponent', () => {
  let component: EventCreateComponent;
  let fixture: ComponentFixture<EventCreateComponent>;
  const eventService = {
    create: jasmine.createSpy('create')
  };


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [{
        provide: EventService,
        useValue: eventService
      }],

      declarations: [EventCreateComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(EventCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onSubmit should call service', () => {
    component.startDateNew = new NgbDate(2022, 11, 22);
    component.endDateNew = new NgbDate(2022, 11, 23);
    component.startTimeNew = new NgbTime(20, 15);
    component.endTimeNew = new NgbTime(12, 0);
    component.newEvent = {
      id: undefined,
      name: 'newEvent',
      category: 'newHipHop',
      startDate: new Date(),
      endDate: new Date()
    };
    component.onSubmit(null);

    expect(eventService.create).toHaveBeenCalledWith({
      id: undefined,
      name: 'newEvent',
      category: 'newHipHop',
      startDate: new Date(2022, 11, 22, 20, 15),
      endDate: new Date(2022, 11, 23, 12, 0)
    });
  });
});

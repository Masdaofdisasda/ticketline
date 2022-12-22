import {
  ComponentFixture,
  fakeAsync,
  flush,
  TestBed,
} from '@angular/core/testing';

import { EventCreateComponent } from './event-create.component';
import { EventService } from '../../../services/event.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { EventDto } from '../../../dto/event.dto';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DateTimePickerComponent } from '../../shared/date-time-picker/date-time-picker.component';
import { ToastrModule } from 'ngx-toastr';
import { of } from 'rxjs/internal/observable/of';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('EventCreateComponent', () => {
  let component: EventCreateComponent;
  let fixture: ComponentFixture<EventCreateComponent>;
  let router: Router;
  const eventService = {
    create: jasmine.createSpy('create'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        FormsModule,
        ReactiveFormsModule,
        ToastrModule.forRoot(),
        NgbModule,
        BrowserAnimationsModule,
        RouterTestingModule.withRoutes([]),
      ],
      providers: [
        {
          provide: EventService,
          useValue: eventService,
          formBuilder: FormBuilder,
        },
      ],

      declarations: [EventCreateComponent, DateTimePickerComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EventCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.get(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onSubmit should call service', fakeAsync(() => {
    eventService.create.and.returnValue(
      of({
        name: 'newEvent',
        category: 'newHipHop',
        startDate: new Date(2022, 11, 22, 20, 15),
        endDate: new Date(2022, 11, 23, 12, 0),
      } as EventDto)
    );

    const navigateSpy = spyOn(router, 'navigate');

    component.createFormGroup.get('eventName').setValue('newEvent');
    component.createFormGroup.get('eventCategory').setValue('newHipHop');
    component.createFormGroup
      .get('startTime')
      .setValue(new Date(2022, 11, 22, 20, 15));
    component.createFormGroup
      .get('endTime')
      .setValue(new Date(2022, 11, 23, 12, 0));

    component.onSubmit();

    expect(eventService.create).toHaveBeenCalledWith({
      name: 'newEvent',
      category: 'newHipHop',
      startDate: new Date(2022, 11, 22, 20, 15).toISOString(),
      endDate: new Date(2022, 11, 23, 12, 0).toISOString(),
    });

    expect(navigateSpy).toHaveBeenCalledWith(['/event']);
    flush();
  }));
});

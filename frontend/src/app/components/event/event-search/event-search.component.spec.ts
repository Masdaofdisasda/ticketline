import {ComponentFixture, fakeAsync, TestBed, tick,} from '@angular/core/testing';

import {EventSearchComponent} from './event-search.component';
import {FormBuilder, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {EventSearchRequest} from '../../../dto/event-search-request';
import {DateTimePickerComponent} from '../../shared/date-time-picker/date-time-picker.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('EventSearchComponent', () => {
  let component: EventSearchComponent;
  let fixture: ComponentFixture<EventSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule, HttpClientTestingModule],
      providers: [FormBuilder],
      declarations: [EventSearchComponent, DateTimePickerComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EventSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit searchRequest and reset formGroup', fakeAsync(() => {
    expect(component).toBeTruthy();
    spyOn(component.searchRequest, 'emit').and.stub();

    const input = fixture.nativeElement.querySelector(
      '#artistInput'
    ) as HTMLInputElement;
    expect(input).toBeDefined();
    component.searchFormGroup.get('artistName').setValue('Test Artist');
    input.dispatchEvent(new Event('focusin'));
    input.dispatchEvent(new Event('keyup'));
    component.searchFormGroup.markAsDirty();
    expect(component.searchFormGroup.dirty).toBeTruthy();
    fixture.autoDetectChanges();
    tick(500);

    expect(component.searchRequest.emit).toHaveBeenCalledWith({
      artistName: 'Test Artist',
      country: '',
      city: '',
      street: '',
      zipCode: '',
      venueName: '',
      eventHall: '',
      endTime: '',
      startTime: '',
      category: '',
      nameOfEvent: '',
    } as EventSearchRequest);
  }));
});

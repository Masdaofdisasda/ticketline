import {Component, EventEmitter, OnDestroy, OnInit, Output,} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {EventSearchRequest} from '../../../dto/event-search-request';
import {debounceTime, Observable, Subject, takeUntil} from 'rxjs';
import {EventService} from '../../../services/event.service';

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.scss'],
})
export class EventSearchComponent implements OnInit, OnDestroy {
  @Output() searchRequest = new EventEmitter<EventSearchRequest>();

  destroy = new Subject();

  searchFormGroup: FormGroup;

  categories: Observable<string[]>;

  clearDate = new EventEmitter<boolean>();

  constructor(private formBuilder: FormBuilder, private eventService: EventService) {
  }

  ngOnInit(): void {
    this.resetSearchForm();

    this.listenToFormGroupChange();

    this.categories = this.eventService.getCategories();
  }

  ngOnDestroy(): void {
    this.destroy.complete();
  }

  resetSearchForm(): void {
    this.searchFormGroup = this.formBuilder.group({
      artistName: [''],
      country: [''],
      city: [''],
      street: [''],
      zipCode: [''],
      venueName: [''],
      eventHall: [''],
      tonight: [null],
      startTime: [null],
      endTime: [null],
      category: [''],
      nameOfEvent: [''],
    });
    this.clearDate.emit(true);
    this.searchRequest.emit(this.getEventSearchRequestFromFormGroup());
    this.listenToFormGroupChange();
  }

  setDate(tonight: boolean): void {
    const today = new Date();
    if (tonight) {
      this.searchFormGroup.get('startTime').setValue(today.setHours(0, 1, 0));
      this.searchFormGroup.get('endTime').setValue(today.setHours(23, 59, 0));
    } else {
      const nextFriday = new Date(
        today.setDate(today.getDate() + ((7 - today.getDay() + 5) % 7 || 7))
      );
      const nextSunday = new Date(
        nextFriday.getDate() + 2
      );
      this.searchFormGroup.get('startTime').setValue(nextFriday);
      this.searchFormGroup.get('endTime').setValue(nextSunday);
    }
  }

  private listenToFormGroupChange(): void {
    this.searchFormGroup.valueChanges
      .pipe(takeUntil(this.destroy), debounceTime(500))
      .subscribe(() => {
        if (this.searchFormGroup.dirty) {
          this.searchRequest.emit(this.getEventSearchRequestFromFormGroup());
        }
      });
  }

  private getEventSearchRequestFromFormGroup(): EventSearchRequest {
    return {
      artistName: this.searchFormGroup.get('artistName').value,
      country: this.searchFormGroup.get('country').value,
      city: this.searchFormGroup.get('city').value,
      street: this.searchFormGroup.get('street').value,
      zipCode: this.searchFormGroup.get('zipCode').value,
      venueName: this.searchFormGroup.get('venueName').value,
      eventHall: this.searchFormGroup.get('eventHall').value,
      startTime: this.searchFormGroup.get('startTime').value
        ? this.searchFormGroup.get('startTime').value.toISOString()
        : '',
      endTime: this.searchFormGroup.get('endTime').value ? this.searchFormGroup.get('endTime').value.toISOString()
        : '',
      category: this.searchFormGroup.get('category').value,
      nameOfEvent: this.searchFormGroup.get('nameOfEvent').value,
    } as EventSearchRequest;
  }
}

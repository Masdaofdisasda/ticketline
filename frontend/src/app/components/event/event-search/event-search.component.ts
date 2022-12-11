import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {EventSearchRequest} from '../../../dto/event-search-request';
import {debounceTime, Subject, takeUntil} from 'rxjs';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.scss']
})
export class EventSearchComponent implements OnInit, OnDestroy {

  @Output() searchRequest = new EventEmitter<EventSearchRequest>();

  destroy = new Subject();

  searchFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.resetSearchForm();

    this.listenToFormGroupChange();
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
      from: [''],
      tonight: [null],
      startTime: [null],
      genre: [''],
      nameOfEvent: [''],
    });
    this.searchRequest.emit(this.getEventSearchRequestFromFormGroup());
    this.listenToFormGroupChange();
  }

  setDate(tonight: boolean): void {
    const today = new Date();
    if (tonight) {
      this.searchFormGroup.get('startTime').setValue(new NgbDate(today.getUTCFullYear(), today.getUTCMonth() + 1, today.getUTCDate()));
    } else {
      const nextFriday = new Date(
        today.setDate(
          today.getDate() + ((7 - today.getDay() + 5) % 7 || 7)
        )
      );
      this.searchFormGroup.get('startTime')
        .setValue(new NgbDate(nextFriday.getUTCFullYear(), nextFriday.getUTCMonth() + 1, nextFriday.getUTCDate()));
    }
  }

  private listenToFormGroupChange(): void {
    this.searchFormGroup.valueChanges.pipe(
      takeUntil(this.destroy),
      debounceTime(500)
    ).subscribe(() => {
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
      from: this.searchFormGroup.get('from').value,
      startTime: this.searchFormGroup.get('startTime').value ?
        new Date(this.searchFormGroup.get('startTime').value.year,
          this.searchFormGroup.get('startTime').value.month - 1,
          this.searchFormGroup.get('startTime').value.day).toLocaleDateString() : '',
      genre: this.searchFormGroup.get('genre').value,
      nameOfEvent: this.searchFormGroup.get('nameOfEvent').value
    } as EventSearchRequest;
  }
}
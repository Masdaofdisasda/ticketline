import {Component, Input} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ArtistService} from '../../../services/artist.service';
import {VenueService} from '../../../services/venue.service';

@Component({
  selector: 'app-event-performance-create',
  templateUrl: './event-performance-create.component.html',
  styleUrls: ['./event-performance-create.component.scss'],
})
export class EventPerformanceCreateComponent {

  @Input() eventCreateFormGroup: FormGroup = new FormGroup<any>({});
  performanceFormArray: FormArray = new FormArray<any>([]);

  artists = this.artistService.findAll();

  venues = this.venueService.getAll();
  artiste = [];

  constructor(private fb: FormBuilder, private artistService: ArtistService, private venueService: VenueService) {
    this.eventCreateFormGroup.valueChanges.subscribe(value => console.warn(value));
  }

  get performances() {
    return this.eventCreateFormGroup.controls['performances'] as FormArray;
  }

  addPerformance(): void {
    this.performanceFormArray = this.eventCreateFormGroup.get('performances') as FormArray;
    this.performanceFormArray.push(this.fb.group({
      startDateControl: new FormControl(new Date(), Validators.required),
      endDateControl: new FormControl(new Date(), Validators.required),
      artistsControl: new FormControl([], Validators.required),
      venueControl: new FormControl(null, Validators.required),
      roomControl: new FormControl(null, Validators.required),
    }));

    console.log(this.eventCreateFormGroup);
  }

  removePerformance(index: number) {
    this.performanceFormArray.removeAt(index);
    console.log(this.eventCreateFormGroup);
  }
}

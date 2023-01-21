import {Component, Input} from '@angular/core';
import {
  AbstractControl,
  AsyncValidatorFn,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ValidationErrors,
  Validators
} from '@angular/forms';
import {ArtistService} from '../../../services/artist.service';
import {VenueService} from '../../../services/venue.service';
import {PriceCategoryService} from '../../../services/price-category.service';
import {map, Observable} from 'rxjs';
import {PriceCategory} from '../../../dto/venue';

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

  priceCategories: Observable<Array<PriceCategory>>;

  artiste = [];

  constructor(private fb: FormBuilder,
              private artistService: ArtistService,
              private venueService: VenueService,
              private priceCategoryService: PriceCategoryService) {
    this.eventCreateFormGroup.valueChanges.subscribe(value => console.warn(value));
  }

  get performances() {
    return this.eventCreateFormGroup.controls['performances'] as FormArray;
  }

  addPerformance(): void {
    this.performanceFormArray = this.eventCreateFormGroup.get('performances') as FormArray;
    const roomControl = new FormControl(null, Validators.required);
    const pricingsControl = new FormControl({});

    roomControl.valueChanges.subscribe((roomId) => {
      this.priceCategories = this.priceCategoryService.getByRoomId(roomId);
      pricingsControl.setAsyncValidators(this.validatePricings(this.priceCategories));
    });

    this.performanceFormArray.push(this.fb.group({
      startDateControl: new FormControl(new Date(), Validators.required),
      endDateControl: new FormControl(new Date(), Validators.required),
      artistsControl: new FormControl([], Validators.required),
      venueControl: new FormControl(null, Validators.required),
      roomControl,
      pricingsControl
    }));

    console.log(this.eventCreateFormGroup);
  }

  validatePricings(priceCategoryObs: Observable<Array<PriceCategory>>): AsyncValidatorFn {
    return (pricingsControl: FormControl): Observable<ValidationErrors | null> => {
      if (!priceCategoryObs) {
        return null;
      }
      return priceCategoryObs.pipe(
        map((priceCategories) => {
          const assignedValues = priceCategories.map(priceCategory => pricingsControl.value[priceCategory.id])
            .filter(value => value !== undefined && value !== null && value !== '');
          return assignedValues.length === priceCategories.length ? null : {missingValue: true};
        })
      );
    };
  }

  removePerformance(index: number) {
    this.performanceFormArray.removeAt(index);
    console.log(this.eventCreateFormGroup);
  }

  updatePricingMap(_event: Event, id: number, _form: AbstractControl) {
    const event = _event as InputEvent;
    const form = _form as FormGroup;
    form.controls.pricingsControl.value[id] = (event.target as HTMLInputElement).value;
    form.controls.pricingsControl.updateValueAndValidity();
  }
}

import {Component, OnInit, ViewChild} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {EventService} from '../../../services/event.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {ArtistService} from '../../../services/artist.service';
import {EventDto} from '../../../dto/event.dto';
import {PerformanceDto} from '../../../dto/performance.dto';
import {NgbAccordion} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.scss'],
})
export class EventCreateComponent implements OnInit {
  @ViewChild(NgbAccordion)
  accordion: NgbAccordion;
  createFormGroup: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private eventService: EventService,
    private router: Router,
    private notification: ToastrService,
    private artistService: ArtistService
  ) {
  }

  get performances() {
    return (this.createFormGroup.get('performances') as FormArray);
  }

  ngOnInit(): void {
    this.createFormGroup = this.formBuilder.group({
      eventName: new FormControl('', Validators.required),
      eventCategory: new FormControl('', Validators.required),
      startTime: new FormControl(null, Validators.required),
      endTime: new FormControl(null, Validators.required),
      performances: this.formBuilder.array([])
    });
  }

  createEvent() {
    const dto = this.getEventDtoFromFormGroup();
    this.eventService.create(dto).subscribe(
      () => {
        this.notification.success( 'Event \'' + dto.name + '\' was created');
        this.router.navigate(['/event']);
      }
    );
  }

  getEventDtoFromFormGroup(): EventDto {
    const eventDto = {
      name: this.createFormGroup.get('eventName').value,
      category: this.createFormGroup.get('eventCategory').value,
      startDate: this.createFormGroup.get('startTime').value
        ? this.createFormGroup.get('startTime').value.toISOString()
        : '',
      endDate: this.createFormGroup.get('endTime').value
        ? this.createFormGroup.get('endTime').value.toISOString()
        : '',
      performances: []
    } as EventDto;

    const performanceFormArray = this.createFormGroup.get('performances') as FormArray;

    for (const performanceForm of performanceFormArray.controls) {
      eventDto.performances.push({
        startDate: performanceForm.get('startDateControl').value,
        endDate: performanceForm.get('endDateControl').value,
        artists: performanceForm.get('artistsControl').value,
        roomId: performanceForm.get('roomControl').value,
        priceCategoryPricingMap: performanceForm.get('pricingsGroup').value,
        blockedSeats: performanceForm.get('blockedSeatsControl').value
      } as unknown as PerformanceDto);
    }
    return eventDto;
  }

  addPerformance(): void {
    this.accordion.collapseAll();
    const performances = this.createFormGroup.get('performances') as FormArray;
    const roomControl = new FormControl(null, Validators.required);
    const pricingsGroup = new FormGroup({});

    performances.push(this.formBuilder.group({
      startDateControl: new FormControl(new Date(), Validators.required),
      endDateControl: new FormControl(new Date(), Validators.required),
      artistsControl: new FormControl([], Validators.required),
      venueControl: new FormControl(null, Validators.required),
      roomControl,
      pricingsGroup,
      blockedSeatsControl: new FormControl([])
    }));
  }

  removePerformance(index: number) {
    (this.createFormGroup.get('performances') as FormArray).removeAt(index);
  }

  log(subject: any) {
    console.log(subject);
  }
}

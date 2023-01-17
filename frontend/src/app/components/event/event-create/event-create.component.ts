import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators,} from '@angular/forms';
import {EventService} from '../../../services/event.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {of} from 'rxjs/internal/observable/of';
import {ArtistService} from '../../../services/artist.service';
import {EventDto} from '../../../dto/event.dto';
import {PerformanceDto} from '../../../dto/performance.dto';

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.scss'],
})
export class EventCreateComponent implements OnInit {
  createFormGroup: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private eventService: EventService,
    private router: Router,
    private notification: ToastrService,
    private artistService: ArtistService
  ) {
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

  artistSuggestion = (input: string) =>
    input === '' ? of([]) : this.artistService.filterByName(input);
  createEvent() {
    this.eventService.create(this.getEventDtoFromFormGroup()).subscribe(data => {
      this.notification.success(
        `Event: \'${data.name}\' successfully created.`
      );
      this.router.navigate(['/event']);
    });
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
        venue: performanceForm.get('venueControl').value,
        room: performanceForm.get('roomControl').value
      } as unknown as PerformanceDto);
    }
    return eventDto;
  }
}

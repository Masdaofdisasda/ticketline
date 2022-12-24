import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { EventService } from '../../../services/event.service';
import { EventDto } from '../../../dto/event.dto';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs/internal/observable/of';
import { ArtistService } from '../../../services/artist.service';

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
  ) {}

  ngOnInit(): void {
    this.createFormGroup = this.formBuilder.group({
      eventName: new FormControl('', Validators.required),
      eventCategory: new FormControl('', Validators.required),
      startTime: new FormControl(null, Validators.required),
      endTime: new FormControl(null, Validators.required),
    });
  }

  artistSuggestion = (input: string) =>
    input === '' ? of([]) : this.artistService.filterByName(input);

  onSubmit(): void {
    const observable = this.eventService.create({
      name: this.createFormGroup.get('eventName').value,
      category: this.createFormGroup.get('eventCategory').value,
      startDate: this.createFormGroup.get('startTime').value
        ? this.createFormGroup.get('startTime').value.toISOString()
        : '',
      endDate: this.createFormGroup.get('endTime').value
        ? this.createFormGroup.get('endTime').value.toISOString()
        : '',
    } as EventDto);
    observable.subscribe({
      next: (data) => {
        this.notification.success(
          `Event: \'${data.name}\' successfully created.`
        );
        this.router.navigate(['/event']);
      },
      error: (err) => {
        console.log('Error creating Event', err);
        console.log(err);
        this.notification.error('Error creating event: \n' + err.error.errors);
      },
    });
  }
}

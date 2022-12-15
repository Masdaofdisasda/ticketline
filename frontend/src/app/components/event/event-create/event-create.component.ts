import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, NgForm, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {EventDto} from '../../../dtos/event.dto';
import {EventService} from '../../../services/event.service';

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.scss']
})
export class EventCreateComponent implements OnInit {

  createForm: FormGroup = new FormGroup({});

  constructor(private eventService: EventService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.createForm = this.fb.group({
      startDate: new FormControl(null, Validators.required),
      endDate: new FormControl(null, Validators.required),
      eventName: new FormControl(null, Validators.required),
      eventCategory: new FormControl(null, Validators.required)
    })
    ;
  }


  public onSubmit(): void {
    console.log(this.getEventFromFormGroup());
    this.eventService.create(this.getEventFromFormGroup()).subscribe();
  }

  getEventFromFormGroup(): EventDto {
    return {
      name: this.createForm.get('eventName').value,
      category: this.createForm.get('eventCategory').value,
      startDate: this.createForm.get('startDate').value,
      endDate: this.createForm.get('endDate').value,
    } as EventDto;
  }

}

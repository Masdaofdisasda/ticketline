import {Component, OnInit} from '@angular/core';
import {FormBuilder, NgForm} from '@angular/forms';
import {EventService} from '../../../services/event.service';
import {EventDto} from '../../../dto/event.dto';
import {NgbDateStruct, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';
import {Router} from '@angular/router';

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.scss']
})
export class EventCreateComponent implements OnInit {


  newEvent: EventDto = {
    id: undefined,
    name: '',
    category: '',
    startDate: new Date(),
    endDate: new Date()
  };

  startDateNew: NgbDateStruct;

  endDateNew: NgbDateStruct;
  startTimeNew: NgbTimeStruct;
  endTimeNew: NgbTimeStruct;


  constructor(private eventService: EventService, private fb: FormBuilder, private router: Router) {
  }

  ngOnInit(): void {

  }


  public onSubmit(form: NgForm): void {

    this.newEvent.startDate = this.extractDate(this.startDateNew, this.startTimeNew);
    this.newEvent.endDate = this.extractDate(this.endDateNew, this.endTimeNew);
    console.log(this.newEvent);
    this.eventService.create(this.newEvent).subscribe();
    this.router.navigate(['/event']);
  }

  //workaround mit string
  public extractDate(d: NgbDateStruct, t: NgbTimeStruct): Date{
    const month = d.month < 10 ? '0' + d.month : d.month;
    const day = d.day < 10 ? '0' + d.day : d.day;
    const hour = t.hour < 10 ? '0' + t.hour : t.hour;
    const minute = t.minute < 10 ? '0' + t.minute : t.minute;
    const dateString = this.startDateNew.year + '-' + month + '-' + day + 'T' + hour + ':' + minute;
    return new Date(dateString);
  }


}

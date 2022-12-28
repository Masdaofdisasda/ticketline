import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { PerformanceDto } from '../../../dto/performance.dto';
import { ArtistDto } from '../../../dto/artist.dto';
import { ArtistService } from '../../../services/artist.service';

@Component({
  selector: 'app-event-performance-create',
  templateUrl: './event-performance-create.component.html',
  styleUrls: ['./event-performance-create.component.scss'],
})
export class EventPerformanceCreateComponent implements OnInit {
  @Output() performanceList = new EventEmitter<PerformanceDto[]>();
  performanceForm: FormGroup;
  artists: ArtistDto[] = [];

  check = false;

  constructor(private fb: FormBuilder, private artistService: ArtistService) {}

  get performances() {
    return (this.performanceForm.get('performances') as FormArray).controls;
  }

  ngOnInit() {
    this.performanceForm = this.fb.group({
      performances: this.fb.array([]),
    });

    this.artistService.findAll().subscribe({
      next: (data) => {
        this.artists = data;
      },
    });
  }

  onFormSubmit() {
    this.performanceList.emit(this.performanceForm.value.performances);
    this.check = true;
  }

  addPerson() {
    (this.performanceForm.get('performances') as FormArray).push(
      this.fb.group({
        startDate: [],
        endDate: [],
        artist: this.artists[0],
      })
    );
    this.check = false;
  }

  removePerson(i) {
    (this.performanceForm.get('performances') as FormArray).removeAt(i);
  }
}

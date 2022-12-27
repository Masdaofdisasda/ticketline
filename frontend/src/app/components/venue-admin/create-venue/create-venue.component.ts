import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Room, Venue} from 'src/app/dto/venue';
import {VenueService} from 'src/app/services/venue.service';

@Component({
  selector: 'app-create-venue',
  templateUrl: './create-venue.component.html',
  styleUrls: ['./create-venue.component.scss']
})
export class CreateVenueComponent implements OnInit, AfterViewInit {
  @Input()
  venue = new Venue();

  @ViewChild('createVenueForm')
  createVenueForm: NgForm;

  @ViewChild('addRoomForm')
  addRoomForm: NgForm;

  roomToAdd = new Room();

  isEditMode: boolean;

  constructor(private activatedRoute: ActivatedRoute,
              private venueService: VenueService) {
  }

  get confirmText() {
    return this.isEditMode ? 'Edit' : 'Create';
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe((data) => {
      this.isEditMode = data.edit;
      if (this.isEditMode) {
        // query the venue by the provided ID
        this.activatedRoute.params.subscribe(params => {
          this.venueService.getByID(params.id).subscribe({
            next: (venue) => {
              this.venue = venue;
            },
            error: (err) => console.log(err)
          });
        });
      }
    });

  }

  ngAfterViewInit(): void {
  }

  addRoom(e: MouseEvent) {
    if (this.addRoomForm.valid) {
      if (!this.venue.rooms) {
        this.venue.rooms = [];
      }
      this.venue.rooms.push(this.roomToAdd);
      this.roomToAdd = new Room();
    } else {
      e.stopPropagation();
    }
  }

  onCreateVenueClick() {
    if (this.createVenueForm.valid) {
      console.log('posting venue', this.venue);

      this.venueService.createVenue(this.venue).subscribe();
    }
  }


}

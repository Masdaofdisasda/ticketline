import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Room, Venue} from 'src/app/dto/venue';
import {VenueService} from 'src/app/services/venue.service';
import {ToastrService} from 'ngx-toastr';
import {ConfirmDeleteService} from '../../../services/confirm-delete-service.service';
import {cloneDeep, isEqual, unset} from 'lodash';

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
  savedItems: Array<Room>;
  saved: boolean;
  queriedVenue: Venue;

  constructor(private activatedRoute: ActivatedRoute,
              private venueService: VenueService,
              private router: Router,
              private notification: ToastrService,
              private confirm: ConfirmDeleteService) {
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
              if (this.getStoredVenue('lastEditedVenue' + params.id)) {
                this.queriedVenue = venue;
                const queried = cloneDeep(this.queriedVenue);
                unset(queried, 'id');
                const saved = cloneDeep(this.venue);
                unset(saved, 'id');
                if (isEqual(queried, saved)) {
                  this.venue = venue;
                  this.saved = false;
                }
              } else {
                this.venue = venue;
              }
            },
            error: (err) => console.error(err)
          });
        });
      } else {
        this.getStoredVenue('lastCreatedVenue');
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
      this.resetStoredVenue();
      const navigation = () => {
        this.router.navigateByUrl('/admin/venue').then(() => this.notification.info(`${this.confirmText}ed ${this.venue.name}`));
      };

      if (this.isEditMode) {
        this.venueService.editVenue(this.venue).subscribe({
          next: navigation,
          error: err => this.notification.error(err)
        });
      } else {
        this.venueService.createVenue(this.venue).subscribe({
          next: navigation,
          error: err => this.notification.error(err)
        });
      }
    }
  }

  roomUpdated() {
  }

  saveStoredVenue() {
    window.localStorage.setItem(this.isEditMode ? 'lastEditedVenue' + this.venue.id : 'lastCreatedVenue', JSON.stringify(this.venue));
  }

  resetStoredVenue() {
    window.localStorage.setItem(this.isEditMode ? 'lastEditedVenue' + this.venue.id : 'lastCreatedVenue', null);
  }

  onCancelClick() {
    this.confirm.open('Venue', this.venue.name).then(result => {
      if (result) {
        this.resetStoredVenue();
        this.router.navigateByUrl('/admin/venue');
      }
    });
  }

  reset() {
    this.venue = this.queriedVenue || new Venue();
    this.resetStoredVenue();
  }

  deleteRoom(event: MouseEvent, room: Room, index: number) {
    event.stopPropagation();
    this.confirm.open('Room', room.name).then(result => {
      if (result) {
        this.venue.rooms.splice(index, 1);
        this.saveStoredVenue();
      }
    });
  }

  private getStoredVenue(index: string) {
    const stored = window.localStorage.getItem(index);
    if (stored && stored !== 'null') {
      this.venue = JSON.parse(stored);
      this.saved = true;
      return true;
    }
    return false;
  }
}

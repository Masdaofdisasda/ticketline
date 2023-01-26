import {Component, OnInit} from '@angular/core';
import {VenueService} from '../../services/venue.service';
import {Venue} from '../../dto/venue';
import {ConfirmDeleteService} from '../../services/confirm-delete-service.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-venue-admin',
  templateUrl: './venue-admin.component.html',
  styleUrls: ['./venue-admin.component.scss']
})
export class VenueAdminComponent implements OnInit {

  venues: Array<Venue>;

  constructor(private service: VenueService,
              private deleteDialog: ConfirmDeleteService,
              private notification: ToastrService) {
  }

  ngOnInit(): void {
    this.queryVenues();
  }

  deleteVenueClicked(venue: Venue) {
    this.deleteDialog.open('Venue', venue.name)
      .then((confirmed) => {
        if (confirmed) {
          this.service.deleteVenue(venue.id).subscribe(() => {
            this.notification.success('\'' + venue.name + '\' was removed');
            this.queryVenues();
          });
        }
      });
  }

  private queryVenues(): void {
    this.service.getAll().subscribe({
      next: (data) => this.venues = data,
      error: (err) => {
        console.log(err);
        this.notification.error(err);
      }
    });
  }
}

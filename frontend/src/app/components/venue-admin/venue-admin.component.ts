import {Component, OnInit} from '@angular/core';
import {VenueService} from '../../services/venue.service';
import {Venue} from '../../dto/venue';

@Component({
  selector: 'app-venue-admin',
  templateUrl: './venue-admin.component.html',
  styleUrls: ['./venue-admin.component.scss']
})
export class VenueAdminComponent implements OnInit {

  venues: Array<Venue>;

  constructor(private service: VenueService) {
    this.service.getAll().subscribe({
      next: (data) => this.venues = data,
      error: (err) => console.log(err) // TODO
    });
  }

  ngOnInit(): void {
  }

}

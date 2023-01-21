import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Room} from '../../../dto/venue';
import {VenueService} from '../../../services/venue.service';

@Component({
  selector: 'app-show-venue',
  templateUrl: './show-venue.component.html',
  styleUrls: ['./show-venue.component.scss']
})
export class ShowVenueComponent implements OnInit {
  @Input()
  room: Room;

  constructor(private activatedRoute: ActivatedRoute, private venueService: VenueService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe({
      next: (data) => {
        this.venueService.getByID(data.id).subscribe({
          next: value => this.room = value.rooms[0],
          error: err => console.error(err)
        });
      },
      error: err => console.error(err)
    });
  }

}

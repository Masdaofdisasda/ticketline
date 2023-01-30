import {Component, Input, OnInit} from '@angular/core';
import {PerformanceDto} from '../../../dto/performance.dto';
import {ActivatedRoute} from '@angular/router';
import {EventService} from '../../../services/event.service';

@Component({
  selector: 'app-select-performance',
  templateUrl: './select-performance.component.html',
  styleUrls: ['./select-performance.component.scss']
})
export class SelectPerformanceComponent implements OnInit {

  @Input()
  performances: Array<PerformanceDto>;

  constructor(private route: ActivatedRoute,
              private eventService: EventService) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe({
      next: value => {
        const eventId = parseInt(value.get('eventId'), 10);
        this.eventService.getById(eventId).subscribe({
          next: event => this.performances = event.performances,
          error: err => console.error(err)
        });
      },
      error: err => console.error(err)
    });
  }

  artistNames(performance: PerformanceDto): string {
    return performance.artists.map(artist => artist.name).join(', ');
  }

  isInFuture(startDate: Date) {
    return new Date(startDate) >= new Date();
  }
}

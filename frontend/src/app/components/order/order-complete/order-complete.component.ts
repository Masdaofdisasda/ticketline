import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BookingDetail} from '../../../dto/BookingDetail';
import {BookingService} from '../../../services/booking.service';

@Component({
  selector: 'app-order-complete',
  templateUrl: './order-complete.component.html',
  styleUrls: ['./order-complete.component.scss']
})
export class OrderCompleteComponent implements OnInit {

  booking: BookingDetail;

  constructor(
    private route: ActivatedRoute,
    private bookingService: BookingService
    ) { }

  ngOnInit(): void {
    this.route.params.subscribe((data) => {

      const bookingId = this.route.snapshot.paramMap.get('bookingId');
      if (null == bookingId) {
        return;
      }

      this.bookingService.fetchBooking(Number(bookingId))
        .subscribe({
          next: response => {
            this.booking = response;
          }
        });
    });

  }

}

import {Component, OnInit} from '@angular/core';
import {BookingService} from '../../../services/booking.service';
import {BookingItem} from '../../../dto/bookingItem';

@Component({
  selector: 'app-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.scss']
})
export class BookingsComponent implements OnInit {

  public bookings: BookingItem[];

  constructor(
    private bookingService: BookingService,
  ) {
  }

  ngOnInit(): void {
    this.bookingService.fetchBookings()
      .subscribe({
        next: data => {
          this.bookings = data;
        }
      });
  }

  cancelBooking(bookingId: number): void {
    this.bookingService.cancelBooking(bookingId)
      .subscribe({
        next: () => {
          this.ngOnInit();
        }
      });
  }

  purchaseReservation(bookingId: number): void {
    this.bookingService.purchaseReservation(bookingId)
      .subscribe({
        next: () => {
          this.ngOnInit();
        }
      });
  }

}

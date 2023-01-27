import {Component, OnInit} from '@angular/core';
import {BookingService} from '../../../services/booking.service';
import {BookingItem} from '../../../dto/bookingItem';
import {tap} from 'rxjs/operators';

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
      .pipe(tap(data => console.log(data)))
      .subscribe(() => this.ngOnInit());
  }

  purchaseReservation(bookingId: number): void {
    this.bookingService.purchaseReservation(bookingId)
      .subscribe({
        next: () => {
          this.ngOnInit();
        }
      });
  }

  downloadTicket(bookingId): void {
    this.bookingService.downloadAndSave(
      this.bookingService.downloadTickets(bookingId),
      'tickets for booking ' + bookingId
    );
  }

  downloadReceipt(bookingId): void {
    this.bookingService.downloadAndSave(
      this.bookingService.downloadReceipt(bookingId),
      'receipt for booking ' + bookingId
    );
  }

  downloadCancellation(bookingId): void {
    this.bookingService.downloadAndSave(
      this.bookingService.downloadCancellation(bookingId),
      'cancellation for booking ' + bookingId
    );
  }


}

import {Component, OnInit} from '@angular/core';
import {BookingService} from '../../../services/booking.service';
import {BookingItem} from '../../../dto/bookingItem';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.scss']
})
export class BookingsComponent implements OnInit {

  public bookings: BookingItem[];

  constructor(
    private bookingService: BookingService,
    private toastr: ToastrService,
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
      .subscribe(() => {
        this.toastr.success('Booking ' + bookingId + ' was canceled');
        this.ngOnInit();
      });
  }

  downloadTicket(bookingId): void {
    this.bookingService.downloadAndSave(
      this.bookingService.downloadTickets(bookingId),
      'tickets_booking_' + bookingId
    );
  }

  downloadReceipt(bookingId): void {
    this.bookingService.downloadAndSave(
      this.bookingService.downloadReceipt(bookingId),
      'receipt_booking_' + bookingId
    );
  }

  downloadCancellation(bookingId): void {
    this.bookingService.downloadAndSave(
      this.bookingService.downloadCancellation(bookingId),
      'cancellation_booking_' + bookingId
    );
  }


}

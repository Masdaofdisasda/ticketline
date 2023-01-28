import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BookingService} from '../../../services/booking.service';
import {BookingFull} from '../../../dto/bookingFull';
import {TicketFullDto} from '../../../dto/ticketFullDto';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.scss']
})
export class ReservationComponent implements OnInit {

  booking: BookingFull;
  cart: TicketFullDto[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bookingService: BookingService,
    private toastr: ToastrService,
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(() => {

      const bookingId = this.route.snapshot.paramMap.get('bookingId');
      if (null == bookingId) {
        return;
      }

      this.bookingService.fetchFullBooking(Number(bookingId))
        .subscribe({
          next: data => {
            this.booking = data;
            this.cart = JSON.parse(JSON.stringify(data.tickets)); //copy tickets
          }
        });

    });
  }

  isSelected(ticket: TicketFullDto) {
    for (const t of this.cart) {
      if (t.ticketId === ticket.ticketId) {
        return true;
      }
    }
    return false;
  }

  formatTicketPrice(ticket: TicketFullDto): string {
    return String(ticket.price.toFixed(2)) + ' €';
  }

  selectItem(ticket: TicketFullDto) {
    if (this.isSelected(ticket)) {
      for (let i = 0; i < this.cart.length; i++) {
        if (this.cart.at(i).ticketId === ticket.ticketId) {
          this.cart.splice(i, 1);
          return;
        }
      }
    } else {
      this.cart.push(ticket);
    }

  }

  cancelBooking(bookingId: number): void {
    this.bookingService.cancelBooking(bookingId)
      .subscribe(() => {
        this.toastr.success('Reservation ' + bookingId + ' was revoked');
        this.router.navigate(['/user/bookings']);
      });
  }

  purchaseTickets(): void {
    this.bookingService.purchaseReservation(this.booking.bookingId, this.cart)
      .subscribe({
        next: () => {
          this.toastr.success('Selected Tickets were purchased');
          this.router.navigate(['/user/bookings']);
        }
      });
  }

  getTotalAmount() {
    let total = 0;
    for (const ticket of this.cart) {
      total += ticket.price;
    }
    const amount = total === 0 ? '0.00' : String(total.toFixed(2));
    return 'Total: ' + amount + ' €';
  }

  getEvent(ticket: TicketFullDto) {
    let artists = '';
    for (const artist of ticket.performance.artists) {
      artists += artist.name + ' ';
    }
    return 'Event: ' + artists + ' starting on ';
  }
}

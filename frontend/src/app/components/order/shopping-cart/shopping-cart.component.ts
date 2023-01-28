import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ShoppingCartService} from '../../../services/shopping-cart.service';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {BookingService} from '../../../services/booking.service';
import {Ticket} from '../../../dto/ticket';
import {AuthService} from '../../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {of} from 'rxjs/internal/observable/of';


@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent implements OnInit, AfterViewInit {

  @Input()
  checkoutMode = false;

  @Output()
  hoverStart = new EventEmitter<Ticket>();
  @Output()
  hoverEnd = new EventEmitter<Ticket>();

  tickets$: Observable<Ticket[]> = of([]);
  performanceId: number;

  constructor(
    private shoppingCartService: ShoppingCartService,
    private bookingService: BookingService,
    private router: Router,
    public authService: AuthService,
    private toastr: ToastrService) {
  }

  ngOnInit(): void {
    this.shoppingCartService.getItems();
    this.tickets$ = this.shoppingCartService.tickets$;
  }

  ngAfterViewInit(): void {

  }

  isEmpty(): boolean {
    return this.shoppingCartService.isEmpty();
  }

  getHeader(): string {
    if (this.checkoutMode) {
      return 'Your Order:';
    }

    if (this.isEmpty()) {
      return 'Please select Seats';
    } else {
      return 'Your Tickets:';
    }
  }

  getTotalAmount(): string {
    const total = this.shoppingCartService.calculateTotal();
    const amount = total === 0 ? '0.00' : String(total.toFixed(2));
    return 'Total: ' + amount + ' €';
  }

  clearCart(): void {
    this.shoppingCartService.clearCart();
  }

  reserveTickets(): void {
    this.bookingService.reserveTickets(this.shoppingCartService.getItems())
      .subscribe({
        next: data => {
          const orderId = data.bookingId;
          this.toastr.success('Reservation successful');
          this.clearCart();
          this.router.navigate(['order', orderId]);
        }
      });
  }

  purchaseTickets(): void {
    this.bookingService.purchaseTickets(this.shoppingCartService.getItems())
      .subscribe({
        next: data => {
          const orderId = data.bookingId;
          this.toastr.success('Purchase successful');
          this.clearCart();
          this.download(data.bookingId);
          this.router.navigate(['order', orderId]);
        }
      });
  }

  download(bookingId): void {
    this.bookingService.downloadAndSave(
      this.bookingService.downloadTickets(bookingId),
      'tickets_booking_' + bookingId
    );
  }

  onHoverStart(ticket: Ticket): void {
    this.hoverStart.emit(ticket);
  }

  onHoverEnd(ticket: Ticket): void {
    this.hoverEnd.emit(ticket);
  }
}

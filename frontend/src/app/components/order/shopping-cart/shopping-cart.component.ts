import {Component, Input, OnInit} from '@angular/core';
import {ShoppingCartService} from '../../../services/shopping-cart.service';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {BookingService} from '../../../services/booking.service';
import {Ticket} from '../../../dto/ticket';
import {AuthService} from '../../../services/auth.service';


@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent implements OnInit {

  @Input()
  checkoutMode = false;

  tickets$: Observable<Ticket[]>;

  constructor(
    private shoppingCartService: ShoppingCartService,
    private bookingService: BookingService,
    private router: Router,
    public authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.tickets$ = this.shoppingCartService.tickets$;
  }

  isEmpty(): boolean {
    return this.shoppingCartService.isEmpty();
  }

  getHeader(): string {
    if (this.checkoutMode){
      return 'Your order:';
    }

    if (this.isEmpty()){
      return 'Please select seats';
    } else {
      return 'Your tickets:';
    }
  }

  getTotalAmount(): string {
    const total = this.shoppingCartService.calculateTotal();
    const amount = total === 0 ? '0.00' : String(total);
    return 'Total: ' + amount + ' EUR';
  }

  clearCart(): void {
    this.shoppingCartService.clearCart();
  }

  reserveTickets(): void {
    this.bookingService.reserveTickets(this.shoppingCartService.getItems())
      .subscribe({
        next: data => {
          const orderId = data;
          this.clearCart();
          this.router.navigate(['order', orderId]);
        }
      });
  }

  purchaseTickets(): void {
      this.bookingService.purchaseTickets(this.shoppingCartService.getItems())
      .subscribe({
        next: data => {
          const orderId = data;
          this.clearCart();
          this.router.navigate(['order', orderId]);
        }
      });
  }
}
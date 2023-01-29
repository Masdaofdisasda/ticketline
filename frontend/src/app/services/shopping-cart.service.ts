import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Ticket} from '../dto/ticket';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {

  ticketsSubject: BehaviorSubject<Ticket[]> = new BehaviorSubject<Ticket[]>([]);
  tickets$: Observable<Ticket[]> = this.ticketsSubject.asObservable();

  constructor(private authService: AuthService) {
  }

  async addToCart(ticket: Ticket) {
    const tickets = this.getItems();
    tickets.push(ticket);
    localStorage.setItem(this.authService.getUserEmail() + '-cart', JSON.stringify(this.getItems()));
    this.ticketsSubject.next(tickets);
  }

  removeItem(ticket: Ticket) {
    const tickets = this.getItems();
    const newTickets = tickets.filter(ele => ele !== ticket);
    this.ticketsSubject.next(newTickets);
    localStorage.setItem(this.authService.getUserEmail() + '-cart', JSON.stringify(newTickets));
  }

  getItems(): Ticket[] {
    if (this.ticketsSubject.getValue().length === 0) {
      const retrievedObject = localStorage.getItem(this.authService.getUserEmail() + '-cart');
      if (retrievedObject) {
        this.ticketsSubject.next(JSON.parse(retrievedObject));
      }
    }
    return this.ticketsSubject.getValue();
  }

  clearCart() {
    this.ticketsSubject.next([]);
    localStorage.removeItem(this.authService.getUserEmail() + '-cart');
  }

  isEmpty(): boolean {
    const tickets = this.getItems();
    return tickets.length === 0;
  }

  calculateTotal() {
    let total = 0;
    const tickets = this.getItems();
    for (const ticket of tickets) {
      total += ticket.price;
    }
    return this.round2Fixed(total);
  }

  private round2Fixed(x: number): number {
    return Math.round(x * 100) / 100;
  }

}

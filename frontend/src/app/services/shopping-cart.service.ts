import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Ticket} from '../dto/ticket';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {

  ticketsSubject: BehaviorSubject<Ticket[]> = new BehaviorSubject<Ticket[]>([]);
  tickets$: Observable<Ticket[]> = this.ticketsSubject.asObservable();

  constructor() {
  }

  addToCart(ticket: Ticket) {
    const tickets = this.getItems();
    tickets.push(ticket);
    this.ticketsSubject.next(tickets);
    sessionStorage.setItem('cart', JSON.stringify(this.getItems()));
  }

  removeItem(ticket: Ticket) {
    const tickets = this.getItems();
    this.ticketsSubject.next(tickets.filter(ele => ele !== ticket));
    sessionStorage.setItem('cart', JSON.stringify(this.getItems()));
  }

  getItems(): Ticket[] {
    if (!this.ticketsSubject.getValue()) {
      const retrievedObject = sessionStorage.getItem('cart');
      if (retrievedObject) {
        this.ticketsSubject.next(JSON.parse(retrievedObject));
      }
    }

    return this.ticketsSubject.getValue();
  }

  clearCart() {
    this.ticketsSubject.next([]);
    sessionStorage.setItem('cart', JSON.stringify(this.getItems()));
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

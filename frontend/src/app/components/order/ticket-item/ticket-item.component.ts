import {Component, Input, OnInit} from '@angular/core';
import {ShoppingCartService} from '../../../services/shopping-cart.service';
import {Ticket} from '../../../dto/ticket';

@Component({
  selector: 'app-ticket-item',
  templateUrl: './ticket-item.component.html',
  styleUrls: ['./ticket-item.component.scss']
})
export class TicketItemComponent implements OnInit {

  @Input()
  ticket: Ticket;

  constructor(
    private shoppingCartService: ShoppingCartService,
    ) { }

  ngOnInit(): void {
  }

  formatTicketInfo(): string {
    return '1x Sector: ' + this.ticket.sector
    + ', Seat: ' + this.ticket.seatNumber;
  }

  formatTicketPrice(): string {
    return String(this.ticket.price) + ' EUR';
  }

  removeItem() {
    this.shoppingCartService.removeItem(this.ticket);
  }

}

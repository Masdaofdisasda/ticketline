import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
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

  @Input()
  muted: boolean;

  @Input()
  hoverable: boolean;

  @Output()
  delete = new EventEmitter<void>();

  constructor(
    private shoppingCartService: ShoppingCartService,
  ) {
  }

  ngOnInit(): void {
  }

  formatTicketPrice(): string {
    return String(this.ticket.price.toFixed(2)) + ' €';
  }

  removeItem() {
    this.shoppingCartService.removeItem(this.ticket);
    this.delete.emit();
  }

}

import {Component, OnInit} from '@angular/core';
import {ShoppingCartService} from '../../../services/shopping-cart.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-seat-selection',
  templateUrl: './seat-selection.component.html',
  styleUrls: ['./seat-selection.component.scss']
})
export class SeatSelectionComponent implements OnInit {


  constructor(
    private shoppingCartService: ShoppingCartService,
    private route: ActivatedRoute,
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(() => {

      const eventId = this.route.snapshot.paramMap.get('eventId');
      if (!eventId) {
        return;
      }

      //TODO fetch event seats
    });
  }

  addItem() {
    const ticket = {
      performance: 'Event1', sector: 'Hall A', row: 1, column: 1,
      seatNumber: (Math.round(15 + Math.random() * 16)).toString(16), price: 26.99
    };

    this.shoppingCartService.addToCart(ticket);
  }

}

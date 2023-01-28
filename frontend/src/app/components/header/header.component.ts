import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {ShoppingCartService} from '../../services/shopping-cart.service';
import {Observable} from 'rxjs';
import {Ticket} from '../../dto/ticket';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  tickets$: Observable<Ticket[]>;

  constructor(public authService: AuthService,
              private shoppingCartService: ShoppingCartService,) {
  }

  ngOnInit() {
    this.tickets$ = this.shoppingCartService.tickets$;
  }
}

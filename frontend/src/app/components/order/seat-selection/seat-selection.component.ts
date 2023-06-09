import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ShoppingCartService} from '../../../services/shopping-cart.service';
import {ActivatedRoute} from '@angular/router';
import {Seat, SeatState, Sector} from '../../../dto/venue';
import {MultiselectEvent} from '../../room-plan/multiselect-event';
import {RoomPlanComponent} from '../../room-plan/room-plan.component';
import {PerformanceService} from '../../../services/performance.service';
import {PerformanceDto} from '../../../dto/performance.dto';
import {VenueService} from '../../../services/venue.service';
import {ShoppingCartComponent} from '../shopping-cart/shopping-cart.component';
import {NgxSpinnerService} from 'ngx-spinner';
import {Ticket} from '../../../dto/ticket';
import {ColorService} from '../../../services/color.service';

@Component({
  selector: 'app-seat-selection',
  templateUrl: './seat-selection.component.html',
  styleUrls: ['./seat-selection.component.scss']
})
export class SeatSelectionComponent implements OnInit, AfterViewInit {

  @ViewChild(RoomPlanComponent)
  roomPlan: RoomPlanComponent;

  @ViewChild(ShoppingCartComponent)
  shoppingCart: ShoppingCartComponent;

  performance: PerformanceDto;

  private performanceId: number;
  private readonly highlightColor = 'ffffff87';

  constructor(
    private shoppingCartService: ShoppingCartService,
    private route: ActivatedRoute,
    private performanceService: PerformanceService,
    private venueService: VenueService,
    private spinner: NgxSpinnerService,
    private colors: ColorService
  ) {
  }

  get shoppingCartHeight() {
    return visualViewport.height - document.querySelector('app-shopping-cart').getBoundingClientRect().y - 20;
  }

  ngOnInit(): void {
    this.spinner.show('room-plan-spinner');
    this.route.params.subscribe(() => {
      this.performanceId = parseInt(this.route.snapshot.paramMap.get('performanceId'), 10);
      if (!this.performanceId) {
        return;
      }
      this.performanceService.getRoomPlanPerformance(this.performanceId).subscribe({
        next: data => {
          this.performance = data;
          this.spinner.hide('room-plan-spinner');
          setTimeout(() => this.highlightInCart(this.shoppingCartService.getItems()), 10);
        },
        error: err => console.error(err)
      });

    });
  }

  ngAfterViewInit(): void {
    this.shoppingCart.performanceId = this.performanceId;
    this.shoppingCart.tickets$.subscribe((tickets) => {
      if (this.performance) {
        this.highlightInCart(tickets);
      }
    });
  }


  highlightInCart(tickets: Ticket[]): void {
    this.resetSelected();
    tickets.forEach(ticket => {
      if (ticket.performanceId === this.performanceId) {
        this.roomPlan.setOutline(this.colors.offsetHue(this.roomPlan.getSeat(ticket.seat.colNumber, ticket.seat.rowNumber).color, 110),
          ticket.column, ticket.row);
      }
    });
  }

  onMultiSelectEnd(selected: Array<Seat>) {
    selected.forEach(seat => {
      this.toggle(seat);
    });
  }

  onMultiSelectChange(event: MultiselectEvent) {
    event.added.forEach(seat => this.selectSeat(seat));
    event.removed.forEach(seat => this.roomPlan.removeOutline(seat.colNumber, seat.rowNumber));
  }

  shoppingCartHoverStart(ticket: Ticket) {
    if (ticket.performanceId === this.performanceId) {
      this.roomPlan.setColor(this.highlightColor, ticket.column, ticket.row);
    }
  }

  shoppingCardHoverEnd(ticket: Ticket) {
    if (ticket.performanceId === this.performanceId) {
      this.roomPlan.setColor(this.roomPlan.getSeat(ticket.seat.colNumber, ticket.seat.rowNumber).color, ticket.column, ticket.row);
    }
  }

  private getSectorOfSeat(seat: Seat): Sector {
    for (const sector of this.performance.room?.sectors) {
      if (sector.seats.includes(seat)) {
        return sector;
      }
    }
    return null;
  }

  private async addSeatToCart(seat: Seat) {
    const sector = this.getSectorOfSeat(seat);

    this.shoppingCartService.addToCart({
      performanceId: this.performanceId, sector: sector.name, row: seat.rowNumber, column: seat.colNumber,
      seatNumber: `${seat.rowName || seat.rowNumber}:${seat.colName || seat.colNumber}`,
      price: sector.priceCategory.pricing,
      seat
    } as Ticket);
  }

  private resetSelected() {
    this.performance?.room?.sectors?.forEach(sector =>
      sector.seats.forEach(seat => this.roomPlan?.removeOutline(seat.colNumber, seat.rowNumber)));
  }

  private toggle(seat: Seat) {
    if (!(seat.state === SeatState.free || seat.state === SeatState.unset)) {
      return;
    }

    const filtered = this.shoppingCartService.getItems().filter((ticket: Ticket) =>
        ticket.row === seat.rowNumber && ticket.column === seat.colNumber && this.performanceId === ticket.performanceId)[0];
    if (!filtered) {
      this.addSeatToCart(seat);
    } else {
      this.shoppingCartService.removeItem(filtered);
    }
  }

  private async selectSeat(seat: Seat) {
    if (!(seat.state === SeatState.free || seat.state === SeatState.unset)) {
      return;
    }
    this.roomPlan.setOutline(this.colors.offsetHue(this.roomPlan.getSeat(seat.colNumber, seat.rowNumber).color, 110) + 'aa',
      seat.colNumber, seat.rowNumber);
  }
}

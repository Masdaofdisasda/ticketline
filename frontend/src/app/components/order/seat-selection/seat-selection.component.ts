import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ShoppingCartService} from '../../../services/shopping-cart.service';
import {ActivatedRoute} from '@angular/router';
import {Seat, Sector} from '../../../dto/venue';
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
  private readonly selectionOutlineColor = 'b8a';
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

  private get selectionOutlineStrength() {
    return this.roomPlan.height * .1;
  }

  ngOnInit(): void {
    this.spinner.show('room-plan-spinner');
    this.route.params.subscribe(() => {
      const performanceId = this.route.snapshot.paramMap.get('performanceId');
      if (!performanceId) {
        return;
      }
      this.performanceService.getRoomPlanPerformance(parseInt(performanceId, 10)).subscribe({
        next: data => {
          this.performance = data;
          this.spinner.hide('room-plan-spinner');
        },
        error: err => console.error(err)
      });

    });
  }

  ngAfterViewInit(): void {
    this.shoppingCart.tickets$.subscribe((tickets) => {
      this.resetSelected();
      tickets.forEach(ticket =>
        this.roomPlan.setOutline(this.selectionOutlineColor, this.selectionOutlineStrength, ticket.column, ticket.row));
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
    this.roomPlan.setColor(this.highlightColor, ticket.column, ticket.row);
  }

  shoppingCardHoverEnd(ticket: Ticket) {
    this.roomPlan.setColor(this.getSectorOfSeat(ticket.seat).priceCategory.color, ticket.column, ticket.row);
  }

  private getSectorOfSeat(seat: Seat): Sector {
    for (const sector of this.performance.room?.sectors) {
      if (sector.seats.includes(seat)) {
        return sector;
      }
    }
    return null;
  }

  private addSeatToCart(seat: Seat) {
    const sector = this.getSectorOfSeat(seat);
    console.log(sector, this.performance);

    this.shoppingCartService.addToCart({
      performance: 'performance', sector: sector.name, row: seat.rowNumber, column: seat.colNumber,
      seatNumber: `${seat.rowName || seat.rowNumber}:${seat.colName || seat.colNumber}`,
      price: sector.priceCategory.pricing,
      seat
    });
  }

  private resetSelected() {
    this.performance?.room?.sectors?.forEach(sector =>
      sector.seats.forEach(seat => this.roomPlan.removeOutline(seat.colNumber, seat.rowNumber)));
  }

  private toggle(seat: Seat) {
    const filtered = this.shoppingCartService.getItems().filter((ticket: Ticket) =>
      ticket.row === seat.rowNumber && ticket.column === seat.colNumber)[0];
    if (!filtered) {
      this.addSeatToCart(seat);
    } else {
      this.shoppingCartService.removeItem(filtered);
    }
  }

  private selectSeat(seat: Seat) {
    this.roomPlan.setOutline(this.colors.offsetHue(this.getSectorOfSeat(seat).priceCategory.color, 90) + 'aa',
      this.selectionOutlineStrength, seat.colNumber, seat.rowNumber);
  }
}

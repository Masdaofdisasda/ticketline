import {AfterContentInit, AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {PriceCategory, Room, Seat, Sector} from 'src/app/dto/venue';
import {MultiselectEvent} from '../../../room-plan/multiselect-event';
import {RoomPlanComponent} from '../../../room-plan/room-plan.component';
import {ConfirmDeleteService} from '../../../../services/confirm-delete-service.service';

@Component({
  selector: 'app-room-element',
  templateUrl: './room-element.component.html',
  styleUrls: ['./room-element.component.scss']
})
export class RoomElementComponent implements OnInit, AfterViewInit, AfterContentInit {

  @Input()
  room: Room;

  @ViewChild('nameInput')
  nameInput: ElementRef<HTMLInputElement>;

  @ViewChild('sectorList')
  sectorList: ElementRef<HTMLDivElement>;

  @ViewChild(RoomPlanComponent)
  roomPlan: RoomPlanComponent;

  @Output()
  updated = new EventEmitter<void>();

  selectedIndex = -1;
  currentlySelected = new Array<Seat>();
  readonly unselectedColor = '#e9eaec';
  readonly unselectedID = -1;
  readonly unselectedSector = {id: this.unselectedID, name: '', pricing: .0, priceCategory: new PriceCategory()};
  selectedSector = new BehaviorSubject<Sector>(this.unselectedSector);

  constructor(private confirmDelete: ConfirmDeleteService) {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.selectedSector.subscribe({next: () => this.update()});
    this.update();
  }

  ngAfterContentInit(): void {
  }

  selectSector(target: EventTarget, sector: Sector, index: number) {
    const element = target as Element;
    // if clicked sector was already selected, select no sector (indicated by unselectedID)
    if (element.classList.contains('active')) {
      element.classList.remove('active');
      this.selectedIndex = -1;
      this.selectedSector.next(this.unselectedSector);
      return;
    }

    this.selectedIndex = index;
    this.selectedSector.next(sector);
    // eslint-disable-next-line @typescript-eslint/prefer-for-of
    for (let i = 0; i < element.parentNode.children.length; i++) {
      element.parentNode.children[i].classList.remove('active');
    }
    element.classList.add('active');
  }

  addSector() {
    if (!this.room.sectors) {
      this.room.sectors = [];
    }
    // add the new sector to the front of the sectors array
    this.room.sectors = [{
      name: '',
      priceCategory: {name: 'select...', color: 'ffffff', pricing: .0},
    }, ...this.room.sectors];

    // timeout is needed because the ngFor of the sector list takes some time
    setTimeout(() => {
      // select the newly added sector
      this.selectSector(this.sectorList.nativeElement.children[0], this.room.sectors[0], 0);
      // focus on the name input
      this.nameInput.nativeElement.focus();
    }, 10);
  }

  priceCategorySelected(priceCategory: PriceCategory) {
    // update priceCategory
    this.selectedSector.value.priceCategory = priceCategory;
    // notify subscribers of selectedSector
    this.selectedSector.next(this.selectedSector.value);
  }

  onSeatClick(seat: Seat) {
    this.handleSelection(seat);
    // send update to all subscribers
    this.selectedSector.next(this.selectedSector.value);
  }

  handleSelection(seat: Seat, removeAlreadySelected = true) {
    // check if a sector is selected
    if (this.selectedSector.value.id !== this.unselectedID) {
      if (!this.selectedSector.value.seats) {
        // if seats were uninitialized, initialize them
        this.selectedSector.value.seats = [];
      } else if (this.selectedSector.value.seats.find((val) => (val.rowNumber === seat.rowNumber && val.colNumber === seat.colNumber))) {
        if (!removeAlreadySelected) {
          // if already selected elements should not be removed we dont have to do anything
          return;
        }
        // if the selected sector already contains clickedSeat, remove it from the selectedSector
        const newSeats = this.selectedSector.value.seats
          .filter((val) => (val.rowNumber !== seat.rowNumber || val.colNumber !== seat.colNumber));
        // color the clicked seat in unselectedColor
        this.roomPlan.setColor(this.unselectedColor, seat.colNumber, seat.rowNumber);

        this.selectedSector.value.seats = newSeats;
        return;
      }
      // remove seats from sectors that already contain them
      this.room.sectors?.forEach(sector => {
        const index = sector.seats?.findIndex((val) => (val.rowNumber === seat.rowNumber && val.colNumber === seat.colNumber));
        if (index > 0) {
          sector.seats?.splice(index, 1);
        }
      });
      // add clicked seat to selected sector
      this.selectedSector.value.seats.push(seat);
    } else {
      // if no sector was selected color seat in unselectedColor
      this.roomPlan.setColor(this.unselectedColor, seat.colNumber, seat.rowNumber);

      // remove clicked seat from its sector
      this.room.sectors?.forEach((sector) => {
        sector.seats = sector.seats?.filter(
          (val) => (val.rowNumber !== seat.rowNumber || val.colNumber !== seat.colNumber));
      });
    }
  }

  multiSelectChange(event: MultiselectEvent) {
    if (this.selectedSector.value.id === this.unselectedID) {
      event.removed.forEach(seat =>
        this.roomPlan.setColor(this.roomPlan.getSeat(seat.colNumber, seat.rowNumber).color, seat.colNumber, seat.rowNumber));
      event.all.forEach(seat =>
        this.roomPlan.setColor(this.roomPlan.getSeat(seat.colNumber, seat.rowNumber).color + '7b', seat.colNumber, seat.rowNumber));
      return;
    }
    event.removed.forEach(seat => this.roomPlan.setColor(this.unselectedColor, seat.colNumber, seat.rowNumber));
    this.currentlySelected = event.all;
    event.all.forEach(seat => this.roomPlan.setColor(this.selectedSector.value.priceCategory.color + '7b', seat.colNumber, seat.rowNumber));
  }

  multiSelectEnd(seats: Array<Seat>) {
    // this.currentlySelected.forEach(seat => this.roomPlan.setColor(this.unselectedColor, seat.colNumber, seat.rowNumber));
    this.update();
    if (seats.length < 2) {
      return;
    }
    seats.forEach(async seat => this.handleSelection(seat, true));
    // send update to all subscribers
    this.selectedSector.next(this.selectedSector.value);
  }

  hover(seat: Seat) {
    this.update();
    this.roomPlan.setColor(this.selectedSector.value.id === this.unselectedID
      ? 'abcdef' : this.selectedSector.value.priceCategory.color + '7b', seat.colNumber, seat.rowNumber);
  }

  async update(): Promise<void> {
    this.updated.emit();
    if (this.selectedIndex >= 0) {
      this.room.sectors[this.selectedIndex] = this.selectedSector.value;
    }
    this.room.sectors?.forEach(sector => {
      if (!sector.seats) {
        return;
      }
      sector.seats.forEach(seat => {
        this.roomPlan.setColor(sector.priceCategory.color + (this.selectedSector.value.id === this.unselectedID ? '' : '7b'),
          seat.colNumber, seat.rowNumber);
      });
    });

    this.selectedSector.value.seats?.forEach((seat) => {
      this.roomPlan.setColor(this.selectedSector.value.priceCategory.color, seat.colNumber, seat.rowNumber);
    });
  }

  hoverEnd(seat: Seat) {
    this.roomPlan.setColor(this.unselectedColor, seat.colNumber, seat.rowNumber);
    this.update();
  }

  deleteSectorClicked(event: MouseEvent, sector: Sector) {
    event.stopPropagation();
    this.confirmDelete.open('Sector', sector.name).then(result => {
      if (result) {
        this.room.sectors.splice(this.room.sectors.indexOf(sector), 1);
        this.update();
      }
    });
  }
}

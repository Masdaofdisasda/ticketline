import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ArtistService} from '../../../services/artist.service';
import {VenueService} from '../../../services/venue.service';
import {PriceCategoryService} from '../../../services/price-category.service';
import {Observable} from 'rxjs';
import {PriceCategory, Room, Seat, Sector} from '../../../dto/venue';
import {RoomService} from '../../../services/room.service';
import {MultiselectEvent} from '../../room-plan/multiselect-event';
import {RoomPlanComponent} from '../../room-plan/room-plan.component';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'app-event-performance-create',
  templateUrl: './event-performance-create.component.html',
  styleUrls: ['./event-performance-create.component.scss'],
})
export class EventPerformanceCreateComponent implements OnInit, AfterViewInit {
  @ViewChild(RoomPlanComponent)
  roomPlan: RoomPlanComponent;
  // @Input() eventCreateFormGroup: FormGroup = new FormGroup<any>({});
  @Input()
  performanceFormGroup: AbstractControl;

  @Input()
  index: number;

  artists = this.artistService.findAll();

  venues = this.venueService.getAll();

  priceCategories: Observable<Array<PriceCategory>>;
  rooms: Observable<Room[]>;
  selectedRoom$: Observable<Room>;
  selectedRoom: Room;
  seatSelection = [] as Array<number>;

  readonly blockedSeatColor = '#aaa';

  constructor(
    private fb: FormBuilder,
    private artistService: ArtistService,
    private venueService: VenueService,
    private roomService: RoomService,
    private priceCategoryService: PriceCategoryService,
    private spinner: NgxSpinnerService
  ) {}

  get controls() {
    return this.form.controls;
  }

  get form() {
    return this.performanceFormGroup as FormGroup;
  }

  ngOnInit(): void {
    const roomSelected = (roomId) => {
      if (!roomId) {
        return;
      }
      this.priceCategories = this.priceCategoryService.getByRoomId(roomId);
      this.priceCategories.subscribe((pcs) =>
        pcs.forEach((pc) =>
          (this.form.get('pricingsGroup') as FormGroup).addControl(
            pc.id.toString(),
            new FormControl(null, Validators.required)
          )
        )
      );
      this.selectedRoom$ = this.roomService.getById(roomId);
      this.selectedRoom$.subscribe((room) => {
        this.selectedRoom = room;
      });
    };
    this.form.controls.roomControl.valueChanges.subscribe((room) => {
      this.form.controls.blockedSeatsControl.setValue([]);
      roomSelected(room);
    });
    if (this.form.controls.roomControl.value) {
      roomSelected(this.form.controls.roomControl.value);
    }

    this.form.controls.venueControl.valueChanges.subscribe((venue) => {
      console.log('venueChange');
      this.selectedRoom = null;
      this.selectedRoom$ = null;
      this.spinner.show('price-categories-spinner');
      this.performanceFormGroup.get('roomControl').setValue(null);
      this.rooms = this.roomService.getByVenueId(venue.id);
    });

    if (this.form.controls.venueControl.value) {
      this.rooms = this.roomService.getByVenueId(this.form.controls.venueControl.value.id);
    }
  }

  ngAfterViewInit(): void {
    if (this.form.controls.blockedSeatsControl.value) {
      this.selectedRoom$.subscribe(() => {
        setTimeout(() => this.updateRoomPlan(), 400);
      });
    }
  }

  multiSelectEnd(seats: Array<Seat>, _form: AbstractControl) {
    const form = _form as FormGroup;
    this.seatSelection = [];
    seats.forEach((seat) => this.toggleSeat(seat, form));
  }

  toggleSeat(seat: Seat, form: FormGroup) {
    if (form.value.includes(seat.id)) {
      form.value.splice(form.value.indexOf(seat.id), 1);
    } else {
      form.value.push(seat.id);
    }
    this.resetColor(seat, form);
  }

  getSectorOfSeat(seat: Seat): Sector {
    return this.selectedRoom.sectors.filter((sector) =>
      sector.seats.map((seat_) => seat_.id).includes(seat.id)
    )[0];
  }

  resetColor(seat: Seat, _form: AbstractControl) {
    const form = _form as FormGroup;
    if (this.seatSelection.includes(seat.id)) {
      this.roomPlan.setColor(
        this.getSectorOfSeat(seat).priceCategory.color + 'aa',
        seat.colNumber,
        seat.rowNumber
      );
    } else if (form.value.includes(seat.id)) {
      this.roomPlan.setColor(
        this.blockedSeatColor,
        seat.colNumber,
        seat.rowNumber
      );
    } else {
      this.roomPlan.setColor(
        this.roomPlan.getSeat(seat.colNumber, seat.rowNumber).color,
        seat.colNumber,
        seat.rowNumber
      );
    }
  }

  hover(seat: Seat) {
    this.roomPlan.setColor(
      this.roomPlan.getSeat(seat.colNumber, seat.rowNumber).color + 'aa',
      seat.colNumber,
      seat.rowNumber
    );
  }

  multiSelectChange(event: MultiselectEvent, form: AbstractControl) {
    event.removed.forEach((seat) => this.resetColor(seat, form));
    event.added.forEach((seat) => this.hover(seat));
    this.seatSelection = event.all.map((seat) => seat.id);
  }

  updateRoomPlan() {
    this.selectedRoom?.sectors.forEach((sector) => {
      sector.seats.forEach((seat) => {
        if (this.seatSelection.includes(seat.id)) {
          this.roomPlan.setColor(
            sector.priceCategory.color + 'aa',
            seat.colNumber,
            seat.rowNumber
          );
        } else if (
          this.form.get('blockedSeatsControl').value.includes(seat.id)
        ) {
          this.roomPlan.setColor(
            this.blockedSeatColor,
            seat.colNumber,
            seat.rowNumber
          );
        } else {
          this.roomPlan.setColor(
            sector.priceCategory.color,
            seat.colNumber,
            seat.rowNumber
          );
        }
      });
    });
  }
}

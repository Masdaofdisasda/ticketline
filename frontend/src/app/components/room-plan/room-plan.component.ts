import {AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Room, Seat, SeatState} from 'src/app/dto/venue';
import {MultiselectEvent} from './multiselect-event';
import {panzoom} from './panzoom';

@Component({
  selector: 'app-room-plan',
  templateUrl: './room-plan.component.html',
  styleUrls: ['./room-plan.component.scss']
})
export class RoomPlanComponent implements OnInit, AfterViewInit {

  @Input()
  room: Room;

  @ViewChild('panzoom')
  panzoom: ElementRef;

  @ViewChild('wrapper')
  wrapper: ElementRef;

  @ViewChild('svgRoot')
  svgRoot: ElementRef;

  @Input()
  width: number;

  @Input()
  height: number;

  @Input()
  padding = .3;

  @Input()
  borderRadius = .25;

  @Input()
  seatSize = 50;

  @Input()
  drawAllPossible = false;

  @Input()
  multiSelect = true;

  @Output()
  seatClicked = new EventEmitter<Seat>();

  @Output()
  multiSelectChange = new EventEmitter<MultiselectEvent>();

  @Output()
  multiSelectEnd = new EventEmitter<Array<Seat>>();

  @Output()
  hover = new EventEmitter<Seat>();

  @Output()
  hoverEnd = new EventEmitter<Seat>();

  @Input()
  initialColor = '#e9eaec';

  mouseOver = false;
  shiftDown = false;
  mouseDown: Seat;
  lastEntered: Seat;
  selected: Array<Seat>;
  seatGrid = new Array<Array<Seat>>();

  readonly scaleMultiplier = 10;

  constructor() {}

  ngAfterViewInit(): void {
    this.width = this.wrapper.nativeElement.offsetWidth;
    this.height = this.wrapper.nativeElement.offsetHeight;
    this.svgRoot.nativeElement.setAttribute('width', this.width * this.scaleMultiplier);
    this.svgRoot.nativeElement.setAttribute('height', this.height * this.scaleMultiplier);
    this.seatSize = Math.min((this.width) / (this.room.rowSize * (1 + this.padding)),
      (this.height) / (this.room.columnSize * (1 + this.padding))) * this.scaleMultiplier;

    this.panzoom.nativeElement.style.transform = `matrix(${1 / this.scaleMultiplier}, 0, 0, ${1 / this.scaleMultiplier}, 0, 0)`;

    this.panzoom.nativeElement.style.top = -this.height * this.scaleMultiplier * 0.45 + 'px';
    this.panzoom.nativeElement.style.left = -this.width * this.scaleMultiplier * 0.45 + 25 + 'px';

    this.seatGrid = Array.from({length: this.room.rowSize}, () =>
      Array.from({length: this.room.columnSize}, () => null)
    );

    // check if all possible seats should be drawn or only already existing ones
    if (this.drawAllPossible) {
      // draw all possible seats by rooms size
      for (let column = 0; column < this.room.rowSize; column++) {
        for (let row = 0; row < this.room.columnSize; row++) {
          this.addSeat({colNumber: column, rowNumber: row, state: SeatState.free});
        }
      }
    } else {
      setTimeout(() => {
        // draw all existing seats in their corresponding color
        this.room.sectors?.forEach(sector => {
          sector.seats?.forEach(seat => {
            this.addSeat(seat, sector.priceCategory.color, sector.priceCategory.pricing);
          });
        });
      }, 0);
    }

    const stageText = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    stageText.textContent = 'STAGE';
    stageText.setAttribute('fill', '#999');
    stageText.setAttribute('letter-spacing', (4).toString());
    stageText.setAttribute('font-size', (15 * this.scaleMultiplier).toString());
    this.svgRoot.nativeElement.appendChild(stageText);
    const bBox = stageText.getBBox();
    const textX = this.seatSize * (1 + this.padding) * this.room.rowSize - bBox.width / 2 + this.seatSize * 4;
    const textY = this.seatSize * (1 + this.padding) * this.room.columnSize / 2 - bBox.height / 2;
    stageText.setAttribute('x', (textX).toString());
    stageText.setAttribute('y', (textY).toString());
    stageText.setAttribute('transform', `rotate(270 ${textX + bBox.width / 2} ${textY + bBox.height / 2})`);
    stageText.style.userSelect = 'none';

    this.svgRoot.nativeElement.addEventListener('mouseenter', () => {
      this.mouseOver = true;
    }, {passive: true});

    // add mouseup-listener for dragging
    this.svgRoot.nativeElement.addEventListener('mouseup', () => {
      // when mouse is up emit multiselect event and update mousedown
      if (this.mouseDown) {
        this.multiSelectEnd.emit(this.selected);
        this.mouseDown = null;
      }
    }, {passive: true});

    this.svgRoot.nativeElement.parentElement.addEventListener('mouseleave', () => {
      this.mouseOver = false;
      if (this.mouseDown) {
        this.mouseDown = null;
        this.multiSelectChange.emit({
          added: [],
          removed: this.selected,
          all: []
        });
        this.multiSelectEnd.emit([]);
        this.selected = [];
      }
    }, {passive: true});

    // initialize the element to be panable and zoomable
    panzoom(this.panzoom.nativeElement,
      {
        bound: 'none',
        panCondition: () => this.shiftDown,
        zoomCondition: () => true,
        scaleMin: .3 / this.scaleMultiplier,
        scaleMax: 100 / this.scaleMultiplier,
        initialScale: 1 / this.scaleMultiplier
      },
      this.wrapper.nativeElement);
  }

  ngOnInit(): void {
    document.addEventListener('keydown', (e: KeyboardEvent) => {
      if (e.key === 'Shift') {
        this.shiftDown = true;
        if (this.mouseOver || e.target === document.body) {
          e.preventDefault();
        }
      }
    });

    document.addEventListener('keyup', (e: KeyboardEvent) => {
      if (e.key === 'Shift') {
        this.shiftDown = false;
        if (this.mouseOver || e.target === document.body) {
          e.preventDefault();
        }
      }
    });
  }

  setColor(color: string, column: number | string, row: number | string) {
    document.querySelector(`rect[row="${row}"][column="${column}"]`).setAttribute('fill', '#' + color.replace('#', ''));
  }

  setOutline(color: string, column: number, row: number, strength?: number) {
    strength = strength || this.seatSize * .2;
    document.querySelector(`rect[row="${row}"][column="${column}"]`).setAttribute('stroke', '#' + color.replace('#', ''));
    document.querySelector(`rect[row="${row}"][column="${column}"]`).setAttribute('stroke-width', strength.toString());
  }

  removeOutline(column: number, row: number) {
    document.querySelector(`rect[row="${row}"][column="${column}"]`)?.removeAttribute('stroke');
  }

  addSeat(seat: Seat, color?: string, pricing?: number) {
    seat.color = color;
    seat.pricing = pricing;
    this.seatGrid[seat.colNumber][seat.rowNumber] = seat;
    const rect = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
    rect.setAttribute('x', Math.round(((this.seatSize + this.padding * this.seatSize) * seat.colNumber)).toString());
    rect.setAttribute('y', Math.round(((this.seatSize + this.padding * this.seatSize) * seat.rowNumber)).toString());
    rect.setAttribute('width', (this.seatSize).toString());
    rect.setAttribute('height', (this.seatSize).toString());
    rect.setAttribute('class', 'seat');
    rect.setAttribute('column', seat.colNumber.toString());
    rect.setAttribute('row', seat.rowNumber.toString());
    rect.setAttribute('rx', (this.seatSize * this.borderRadius).toString());
    const computeSeatColor = (col: string, state: SeatState) => {
      if (state === SeatState.free || state === SeatState.unset) {
        return '#' + col.replace('#', '');
      } else {
        return '#' + col.replace('#', '') + '44';
      }
    };

    rect.setAttribute('fill', color ? computeSeatColor(color, seat.state) : this.initialColor);

    rect.addEventListener('click', () => this.seatClicked.emit(seat));
    rect.addEventListener('mouseenter', () => this.hover.emit(seat), {passive: true});
    rect.addEventListener('mouseleave', () => this.hoverEnd.emit(seat), {passive: true});
    if (this.multiSelect) {
      rect.addEventListener('mousedown', () => {
        if (!this.shiftDown) {
          this.mouseDown = this.lastEntered = (seat);
          this.selected = [this.lastEntered];
        }
      }, {passive: true});
      rect.addEventListener('mouseenter', () => {
        // draw listener -> only action if mouse is pressed
        if (this.mouseDown) {
          if (this.lastEntered !== seat) {
            const newSelected = new Array<Seat>();
            // add all new seats to the newSelected array
            for (let columnI = Math.min(this.mouseDown.colNumber, seat.colNumber);
              columnI <= Math.max(this.mouseDown.colNumber,  seat.colNumber); columnI++) {
              for (let rowI = Math.min(this.mouseDown.rowNumber,  seat.rowNumber);
                   rowI <= Math.max(this.mouseDown.rowNumber,  seat.rowNumber); rowI++) {
                  newSelected.push(this.seatGrid[columnI][rowI]);
              }
            }
            // update the last
            this.lastEntered = seat;
            this.multiSelectChange.emit({
              added: newSelected.filter((vec) =>
                !this.selected.some(some => some.rowNumber === vec.rowNumber && some.colNumber === vec.colNumber)),
              removed: this.selected.filter((vec) =>
                !newSelected.some(some => some.rowNumber === vec.rowNumber && some.colNumber === vec.colNumber)),
              all: newSelected
            });
            this.selected = newSelected;
          }
        }
      }, {passive: true});
    }
    // add created rect to element
    this.svgRoot.nativeElement.appendChild(rect);
  }

  public getSeat(col: number, row: number) {
    return this.seatGrid[col][row];
  }
}

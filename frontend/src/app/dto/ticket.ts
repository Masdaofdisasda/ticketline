import {Seat} from './venue';

export class Ticket {
  performanceId: number;
  sector: string;
  row: number;
  column: number;
  seatNumber: string;
  price: number;

  seat?: Seat;
}

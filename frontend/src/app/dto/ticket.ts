import {Seat} from './venue';

export class Ticket {
  performance: string;
  sector: string;
  row: number;
  column: number;

  seatNumber: string;
  price: number;

  seat?: Seat;
}

import {Ticket} from './ticket';
import {Seat} from './venue';
import {PerformanceDto} from './performance.dto';

export class TicketFullDto {
  ticketId: number;
  price: number;
  seat: Seat;
  performance: PerformanceDto;
}

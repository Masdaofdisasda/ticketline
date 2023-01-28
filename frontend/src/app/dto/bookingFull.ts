import {Ticket} from './ticket';
import {TicketFullDto} from './ticketFullDto';

export class BookingFull {
  bookingId: number;
  tickets: TicketFullDto[];
}

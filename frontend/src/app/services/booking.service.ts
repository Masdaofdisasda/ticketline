import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Ticket} from '../dto/ticket';
import {BookingDetail} from '../dto/BookingDetail';


@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private baseUri: string = this.globals.backendUri + '/booking';

  constructor(
    private http: HttpClient,
    private globals: Globals
  ) {
  }

  reserveTickets(reservations: Ticket[]): Observable<number> {
    return this.http.post<number>(this.baseUri + '/reservations', reservations);
  }

  purchaseTickets(purchases: Ticket[]): Observable<number> {
    return this.http.post<number>(this.baseUri + '/purchases', purchases);
  }

  fetchBooking(bookingId: number): Observable<BookingDetail> {
    return this.http.get<BookingDetail>(this.baseUri + '/' + bookingId);
  }
}

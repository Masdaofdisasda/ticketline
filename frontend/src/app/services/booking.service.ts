import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Ticket} from '../dto/ticket';
import {BookingDetail} from '../dto/bookingDetail';
import {BookingItem} from '../dto/bookingItem';


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

  fetchBookings(): Observable<BookingItem[]> {
    return this.http.get<BookingItem[]>(this.baseUri);
  }

  cancelBooking(bookingId: number): Observable<void> {
    return this.http.delete<void>(this.baseUri + '/' + bookingId);
  }

  purchaseReservation(bookingId: number): Observable<void> {
    return this.http.put<any>(this.baseUri + '/' + bookingId, {});
  }
}

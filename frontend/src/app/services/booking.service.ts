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

  purchaseTickets(purchases: Ticket[]): Observable<BookingDetail> {
    return this.http.post<BookingDetail>(this.baseUri + '/purchases', purchases);
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

  downloadTickets(bookingId: number): Observable<Blob> {
    return this.http.get(`${this.baseUri}/${bookingId}/ticketsPdf`, {responseType: 'blob'});
  }
  downloadReceipt(bookingId: number): Observable<Blob> {
    return this.http.get(`${this.baseUri}/${bookingId}/receiptPdf`, {responseType: 'blob'});
  }
  downloadCancellation(bookingId: number): Observable<Blob> {
    return this.http.get(`${this.baseUri}/${bookingId}/cancellationPdf`, {responseType: 'blob'});
  }

  downloadAndSave(download: Observable<Blob>, filename: string): void {
    download.subscribe({
      next: value => {
        this.saveData(value, 'application/pdf', 'Ticketline_' + filename + '.pdf');
      }
    });
  }

  private saveData(content, type, fileName) {
    const a = document.createElement('a');
    document.body.appendChild(a);
    a.style.display = 'none';
    const blob = new Blob([content], {type});
    const url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = fileName;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}

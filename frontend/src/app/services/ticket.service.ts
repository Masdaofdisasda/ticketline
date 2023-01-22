import { Injectable } from '@angular/core';
import { Globals } from '../global/globals';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TicketValidationDto} from '../dto/ticketValidation.dto';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private baseUri: string = this.globals.backendUri + '/ticket';
  private status: string;

  constructor(private http: HttpClient, private globals: Globals) {}

  validateTicket(hash: string): Observable<TicketValidationDto> {
    const params = new HttpParams().append('hash', hash);

    console.log('calling ' + this.baseUri + '/validate with param: ' + hash);
    return this.http.get<TicketValidationDto>(this.baseUri + '/validate', { params });
  }
}

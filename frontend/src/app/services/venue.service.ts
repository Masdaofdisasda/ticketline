import {Injectable} from '@angular/core';
import {Venue} from '../dto/venue';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VenueService {

  private venueBaseUri: string = this.globals.backendUri + '/venue';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  public getByID(id: number): Observable<Venue> {
    return this.httpClient.get<Venue>(`${this.venueBaseUri}/${id}`);
  }

  public createVenue(venue: Venue): Observable<Venue> {
    return this.httpClient.post<Venue>(this.venueBaseUri, venue);
  }

  public getAll(): Observable<Array<Venue>> {
    return this.httpClient.get<Array<Venue>>(this.venueBaseUri);
  }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Room} from '../dto/venue';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private roomBaseUri: string = this.globals.backendUri + '/room';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  public getById(id: number): Observable<Room> {
    return this.httpClient.get<Room>(`${this.roomBaseUri}/${id}`);
  }
}

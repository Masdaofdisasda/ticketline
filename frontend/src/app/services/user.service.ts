import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import { User } from '../dto/user.dto';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private userBaseUri: string = this.globals.backendUri + '/user';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Get list of locked users.
   *
   */
  getLocked(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userBaseUri + '/locked');
  }

  /**
   * Unlock a user.
   *
   */
  unlock(userId: number) {
    return this.httpClient.put(
      this.userBaseUri + '/' + userId + '/accountNonLocked',
      { accountNonLocked: true }
    );
  }
}

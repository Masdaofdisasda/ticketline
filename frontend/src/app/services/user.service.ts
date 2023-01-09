import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
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
   * Get list of users.
   *
   */
  getUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userBaseUri + '/all');
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

  /**
   * Lock a user.
   *
   */
  lock(userId: number) {
    return this.httpClient.put(
      this.userBaseUri + '/' + userId + '/accountNonLocked',
      { accountNonLocked: false }
    );
  }

  /**
   * get data of the currently logged in user
   */
  fetchUser(): Observable<User> {
    return this.httpClient.get<User>(this.userBaseUri + '/');
  }

  /**
   * updates user data
   *
   * @param user the data to update
   */
  updateUser(user: User): Observable<User> {
    return this.httpClient.put<User>(this.userBaseUri + '/' + user.id, user);
  }

  deleteUser(userId: number): Observable<void> {
    return this.httpClient.delete<void>(this.userBaseUri + '/' + userId);
  }

  /**
   * Send reset password mail to user.
   *
   */
  resetPassword(email: string) {
    const params = new HttpParams().append('email', email);
    return this.httpClient.post(this.userBaseUri + '/resetPassword', null, {
      params,
    });
  }

  /**
   * Change password of a user.
   *
   * */
  changePassword(password: string, token: string) {
    console.log({
      newPassword: password,
      token,
    });
    return this.httpClient.post(this.userBaseUri + '/savePassword', {
      newPassword: password,
      token,
    });
  }
}

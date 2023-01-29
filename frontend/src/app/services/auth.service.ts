import {Injectable} from '@angular/core';
import {AuthRequest} from '../dto/auth-request';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {Globals} from '../global/globals';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private authBaseUri: string = this.globals.backendUri + '/authentication';

  constructor(private httpClient: HttpClient, private globals: Globals, private router: Router) {
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient
      .post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(tap((authResponse: string) => this.setToken(authResponse)));
  }

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return (
      !!this.getToken() &&
      this.getTokenExpirationDate(this.getToken()).valueOf() >
      new Date().valueOf()
    );
  }

  /**
   * Checks if current logged-in user is an admin
   */
  isAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }

  /**
   * Logs out user from current session
   */
  logoutUser(): void {
    localStorage.removeItem('authToken');
    this.router.navigate(['/']).then(() => window.location.reload());
  }

  getToken(): string {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user email based on the current token
   */
  getUserEmail() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const email: string[] = decoded.sub;
      return email;
    }
    return '';
  }

  /**
   * Returns the user role based on the current token
   */
  private getUserRole(): string {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
    if (localStorage.getItem('-cart')) {
      localStorage.setItem(this.getUserEmail() + '-cart', localStorage.getItem('-cart'));
      localStorage.removeItem('-cart');
    }
  }

  private getTokenExpirationDate(token: string): Date {
    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }
}

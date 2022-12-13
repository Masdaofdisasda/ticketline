import { Injectable } from '@angular/core';
import { RegistrationRequest } from '../dto/registration-request';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {
  private registrationBaseUri: string = this.globals.backendUri + '/user';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Register the user.
   *
   * @param registrationRequest User data
   */
  registerUser(registrationRequest: RegistrationRequest): Observable<string> {
    return this.httpClient.post(this.registrationBaseUri, registrationRequest, {
      responseType: 'text',
    });
  }
}

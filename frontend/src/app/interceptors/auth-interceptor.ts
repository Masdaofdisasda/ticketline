import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { catchError, Observable, throwError } from 'rxjs';
import { Globals } from '../global/globals';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private globals: Globals,
    private toastr: ToastrService
  ) { }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const authUri = this.globals.backendUri + '/authentication';

    // Do not intercept authentication requests
    if (req.url === authUri) {
      return next.handle(req);
    }

  //  const authReq = req.clone({
  //    headers: req.headers.set(
  //      'Authorization',
  //      'Bearer ' + this.authService.getToken()
  //    ),
  //  });

    return next.handle(this.authService.getToken() ? req : req).pipe(
      catchError((error: any) => {
        let errorMsg = '';
        if (error.error instanceof ErrorEvent) {
          console.log('This is client side error');
          errorMsg = `Error: ${error.error.message}`;
        } else {
          console.log('This is server side error');
          errorMsg = `Error Code: ${error.status},  Message: ${error.error}`;
        }
        console.log(errorMsg);
        this.toastr.error(errorMsg);
        return throwError(() => error);
      })
    );
  }
}

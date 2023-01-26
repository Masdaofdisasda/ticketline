import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse,} from '@angular/common/http';
import {AuthService} from '../services/auth.service';
import {catchError, Observable, throwError} from 'rxjs';
import {Globals} from '../global/globals';
import {ToastrService} from 'ngx-toastr';
import {tap} from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private globals: Globals,
    private toastr: ToastrService
  ) {
  }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const authUri = this.globals.backendUri + '/authentication';

    // Do not intercept authentication requests
    if (req.url === authUri) {
      return next.handle(req);
    }

    const authReq = req.clone({
      headers: req.headers.set(
        'Authorization',
        'Bearer ' + this.authService.getToken()
      ),
    });

    return next.handle(this.authService.getToken() ? authReq : req).pipe(
      tap((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse && event.status === 201) {
        }
      }),
      catchError((error: any) => {
        let errorMsg = '';
        if (error.error instanceof ErrorEvent) {
          console.log('This is client side error');
          errorMsg = `Error: ${error.error.message}`;
        } else {
          console.log('This is server side error');
          errorMsg = `Error Code: ${error.status} \n Message: ${error.error}`;
          console.log(error.error);
          if (error.error === 'Invalid authorization header or token') {
            //remove token
            localStorage.removeItem('authToken');
            console.log(localStorage.getItem('authToken'));
          }
        }
        console.log(errorMsg);
        this.toastr.error(errorMsg);
        return throwError(() => error);
      })
    );
  }
}

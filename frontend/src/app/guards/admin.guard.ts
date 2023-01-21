import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  canActivate(): boolean {
    if (this.authService.isLoggedIn()) {
      if (this.authService.isAdmin()) {
        return true;
      } else {
        this.toastr.error('insufficient permissions');
        return false;
      }
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}

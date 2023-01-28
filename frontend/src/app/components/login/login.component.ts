import {Component, OnInit} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators,} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {Location} from '@angular/common';
import {AuthRequest} from '../../dto/auth-request';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm: UntypedFormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    private router: Router,
    private _location: Location) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  ngOnInit() {
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  loginUser() {
    this.submitted = true;
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(
        this.loginForm.controls.email.value,
        this.loginForm.controls.password.value
      );
      this.authenticateUser(authRequest);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the message page
   *
   * @param authRequest authentication data from the user login form
   */
  private authenticateUser(authRequest: AuthRequest) {
    console.log('Try to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        this.router.navigate(['/news']);
      },
      error: (error) => {
        console.log('Could not log in due to:');
        console.log(error);
        console.log(error.status);
        this.error = true;
        let errorObject;
        try {
          errorObject = JSON.parse(error.error);
        } catch (e) {
        }
        if (errorObject && errorObject.error === 'Forbidden') {
          this.errorMessage =
            'Could not log in with these credentials. Please check email and password.';
        } else {
          this.errorMessage = error.error;
        }
      },
    });
  }
}

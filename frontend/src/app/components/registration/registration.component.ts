import { Component, OnInit } from '@angular/core';
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegistrationRequest } from '../../dto/registration-request';
import { AuthRequest } from '../../dto/auth-request';
import { RegistrationService } from 'src/app/services/registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit {
  registrationForm: UntypedFormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    private registrationService: RegistrationService,
    private router: Router
  ) {
    this.registrationForm = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
    });
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  ngOnInit() {}

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  registerUser() {
    this.submitted = true;
    if (this.registrationForm.valid) {
      const registrationRequest: RegistrationRequest = new RegistrationRequest(
        this.registrationForm.controls.email.value,
        this.registrationForm.controls.password.value,
        this.registrationForm.controls.firstName.value,
        this.registrationForm.controls.lastName.value
      );
      this.createUser(registrationRequest);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Send user data to the registrationService. If the registration was successful, the user will be authenticated.
   *
   * @param registrationRequest user data from the user registration form
   */
  private createUser(registrationRequest: RegistrationRequest) {
    console.log('Try to register user: ' + registrationRequest.email);
    this.registrationService.registerUser(registrationRequest).subscribe({
      next: () => {
        console.log(
          'Successfully registered user: ' + registrationRequest.email
        );
        this.authenticateUser(
          new AuthRequest(
            registrationRequest.email,
            registrationRequest.password
          )
        );
      },
      error: (error) => {
        console.log('Could not sign up due to:');
        this.error = true;
        const unpackedError = JSON.parse(error.error);
        if (unpackedError && unpackedError.errors) {
          this.errorMessage = unpackedError.errors[0];
        } else if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      },
    });
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
        console.log('Successfully logged in user: ' + authRequest.email);
        this.router.navigate(['/message']);
      },
      error: (error) => {
        console.log('Could not log in due to:');
        console.log(error);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      },
    });
  }
}

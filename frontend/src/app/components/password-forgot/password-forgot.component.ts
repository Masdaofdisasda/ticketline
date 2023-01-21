import { Component, OnInit } from '@angular/core';
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GenericResponse } from 'src/app/dto/generic-response.dto';
import { UserService } from 'src/app/services/user.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-password-forgot',
  templateUrl: './password-forgot.component.html',
  styleUrls: ['./password-forgot.component.scss'],
})
export class PasswordForgotComponent implements OnInit {
  resetPasswordForm: UntypedFormGroup;
  // After first submission attempt, form validation will start
  submitted = false;

  constructor(
    private formBuilder: UntypedFormBuilder,
    private userService: UserService,
    private toastr: ToastrService,
    private router: Router,
    private _location: Location
  ) {
    this.resetPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  ngOnInit(): void {}
  resetPassword() {
    this.submitted = true;
    if (this.resetPasswordForm.valid) {
      const email = this.resetPasswordForm.controls.email.value;
      this.userService.resetPassword(email).subscribe({
        next: (res: GenericResponse) => {
          if (res.error === null) {
            this.toastr.success('Sent password reset email to : ' + email);
            this.router.navigate(['/login']);
          } else {
            this.toastr.error(
              'An Error occured while trying to reset password for user ' +
                email +
                ': ' +
                res.message
            );
          }
        },
      });
    }
  }
  cancel() {
    this._location.back();
  }
}

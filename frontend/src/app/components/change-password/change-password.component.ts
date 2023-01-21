import { Component, OnInit } from '@angular/core';
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GenericResponse } from 'src/app/dto/generic-response.dto';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss'],
})
export class ChangePasswordComponent implements OnInit {
  changePasswordForm: UntypedFormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';
  token = '';

  constructor(
    private formBuilder: UntypedFormBuilder,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {
    this.changePasswordForm = this.formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }
  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => (this.token = params.token));
  }

  submitPassword() {
    this.submitted = true;
    console.log(this.token);
    if (!this.token || this.token.length === 0) {
      this.toastr.error('No password reset token was found in the URL.');
      return;
    }
    if (this.changePasswordForm.valid) {
      this.userService
        .changePassword(
          this.changePasswordForm.controls.password.value,
          this.token
        )
        .subscribe({
          next: (res: GenericResponse) => {
            if (res.error === null) {
              this.toastr.success('Password changed successfully');
              this.router.navigate(['/login']);
            } else {
              this.toastr.error(
                'An error occured while trying to change password: ' +
                  res.message
              );
            }
          },
        });
    } else {
      console.log('Invalid input');
    }
  }
}

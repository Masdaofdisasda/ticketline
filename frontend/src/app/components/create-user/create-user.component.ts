import { Component, OnInit } from '@angular/core';
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { CreateUserRequest } from 'src/app/dto/create-user-request.dto';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss'],
})
export class CreateUserComponent implements OnInit {
  createUserForm: UntypedFormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(
    private formBuilder: UntypedFormBuilder,
    private userService: UserService,
    private toastr: ToastrService,
    private router: Router
  ) {
    this.createUserForm = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      isAdmin: [false, [Validators.required]],
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
   * Form validation will start after the method is called
   */
  createUser() {
    this.submitted = true;
    if (this.createUserForm.valid) {
      const createUserRequest: CreateUserRequest = new CreateUserRequest(
        this.createUserForm.controls.email.value,
        this.createUserForm.controls.password.value,
        this.createUserForm.controls.firstName.value,
        this.createUserForm.controls.lastName.value,
        this.createUserForm.controls.isAdmin.value
      );
      this.create(createUserRequest);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Send user data to the registrationService. If the registration was successful, the user will be authenticated.
   *
   * @param registrationRequest user data from the user registration form
   */
  private create(createUserRequest: CreateUserRequest) {
    console.log('Try to create user: ' + createUserRequest.email);
    this.userService.createUser(createUserRequest).subscribe({
      next: () => {
        console.log('Successfully created user: ' + createUserRequest.email);
        this.toastr.success(
          'Successfully created user: ' + createUserRequest.email
        );
        this.router.navigate(['admin/users']);
      },
    });
  }
}

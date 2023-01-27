import {Component, OnInit} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../../services/auth.service';
import {RegistrationService} from '../../../services/registration.service';
import {Router} from '@angular/router';
import {UserService} from '../../../services/user.service';
import {User} from '../../../dto/user.dto';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  registrationForm: UntypedFormGroup;
  submitted = false;
  user: User;
  editMode = false;

  constructor(
    private formBuilder: UntypedFormBuilder,
    public authService: AuthService,
    private registrationService: RegistrationService,
    private userService: UserService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.registrationForm = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.userService.fetchUser()
      .subscribe({
        next: response => {
          this.user = response;
          this.registrationForm.setValue({
            email: response.email,
            password: '**********',
            firstName: response.firstName,
            lastName: response.lastName
          });
          this.registrationForm.get('password')?.disable();
          this.setViewMode();
        }
      });
  }

  setEditMode() {
    this.registrationForm.get('email')?.enable();
    this.registrationForm.get('firstName')?.enable();
    this.registrationForm.get('lastName')?.enable();
    this.editMode = true;
  }

  setViewMode() {

    this.registrationForm.setValue({
      email: this.user.email,
      password: '**********',
      firstName: this.user.firstName,
      lastName: this.user.lastName
    });

    this.registrationForm.get('email')?.disable();
    this.registrationForm.get('firstName')?.disable();
    this.registrationForm.get('lastName')?.disable();
    this.editMode = false;
  }

  updateUserData() {
    this.submitted = true;
    if (!this.registrationForm.valid) {
      return;
    }

    const user = {
      id: this.user.id,
      firstName: this.registrationForm.controls.firstName.value,
      lastName: this.registrationForm.controls.lastName.value,
      email: this.registrationForm.controls.email.value
    } as User;

    this.userService.updateUser(user)
      .subscribe({
        next: response => {
          const mailChanged = response.email !== this.user.email;
          this.registrationForm.setValue({
            email: response.email,
            password: '**********',
            firstName: response.firstName,
            lastName: response.lastName
          });
          this.user = response;
          if (mailChanged) {
            this.authService.logoutUser();
            this.router.navigate(['//']);
            this.toastr.success('Email address changed - You were logged out');
          }
          this.toastr.success('User data updated');
        }
      });

    this.registrationForm.get('email')?.disable();
    this.registrationForm.get('firstName')?.disable();
    this.registrationForm.get('lastName')?.disable();
    this.editMode = false;
  }

  deleteUser() {
    this.userService.deleteUser(this.user.id)
      .subscribe({
        next: () => {
          this.authService.logoutUser();
          this.router.navigate(['//']);
          this.toastr.success('User was deleted');
        }
      });
  }
}

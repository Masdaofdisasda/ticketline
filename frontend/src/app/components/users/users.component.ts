import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/dto/user.dto';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { FormControl, FormGroup } from '@angular/forms';
import { GenericResponse } from 'src/app/dto/generic-response.dto';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss'],
})
export class UsersComponent implements OnInit {
  users = [];
  filterTerm = '';
  filteredUsers = [];
  lockedForm = new FormGroup({
    locked: new FormControl('all'),
  });
  constructor(
    private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.getUsers();
    this.lockedForm.valueChanges.subscribe({
      next: () => {
        this.filterUsers();
      },
    });
  }
  changeLocked(user: User) {
    if (user.accountNonLocked) {
      this.lock(user);
    } else {
      this.unlock(user);
    }
  }
  getUsers() {
    this.userService.getUsers().subscribe({
      next: (res) => {
        this.users = res;
        this.filterUsers();
      },
    });
  }
  filterUsers() {
    const re = new RegExp('.*' + this.filterTerm.toLowerCase() + '.*');
    this.filteredUsers = this.users.filter((user) => {
      const lockedFilter: boolean =
        this.lockedForm.value.locked === 'all' ||
        this.lockedForm.value.locked ===
          (user.accountNonLocked ? 'unlocked' : 'locked');
      const name = `${user.firstName} ${user.lastName}`.toLowerCase();
      return (
        (re.test(name) || re.test(user.email.toLowerCase())) && lockedFilter
      );
    });
  }
  isLoggedInUser(user: User) {
    return user.email === this.authService.getUserEmail();
  }

  resetPassword(user: User) {
    this.userService.resetPassword(user.email).subscribe({
      next: (res: GenericResponse) => {
        if (res.error === null) {
          this.toastr.success('Sent password reset email to : ' + user.email);
        } else {
          this.toastr.error(
            'An Error occured while trying to reset password for user ' +
              user.email +
              ': ' +
              res.message
          );
        }
      },
    });
  }

  private unlock(user: User) {
    this.userService.unlock(user.id).subscribe({
      next: (res) => {
        this.toastr.success('Unlocked user: ' + user.email);
        user.accountNonLocked = true;
        this.filterUsers();
      },
    });
  }
  private lock(user: User) {
    this.userService.lock(user.id).subscribe({
      next: (res) => {
        this.toastr.success('Locked user: ' + user.email);
        user.accountNonLocked = false;
        this.filterUsers();
      },
    });
  }
}

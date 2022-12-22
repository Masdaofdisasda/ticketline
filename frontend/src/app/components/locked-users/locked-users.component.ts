import {Component, OnInit} from '@angular/core';
import {User} from 'src/app/dto/user.dto';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-locked-users',
  templateUrl: './locked-users.component.html',
  styleUrls: ['./locked-users.component.scss'],
})
export class LockedUsersComponent implements OnInit {
  lockedUsers = [];

  constructor(
    public authService: AuthService,
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.getLocked();
  }

  /**
   * Returns true if the authenticated user is an admin
   */


  getLocked() {
    this.userService.getLocked().subscribe({
      next: (res) => {
        this.lockedUsers = res;
      },
    });
  }

  unlock(user: User) {
    this.userService.unlock(user.id).subscribe({
      next: (res) => {
        this.lockedUsers = this.lockedUsers.filter((u) => u.id !== user.id);
      },
    });
  }
}

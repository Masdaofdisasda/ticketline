import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ToastrModule } from 'ngx-toastr';
import { of } from 'rxjs/internal/observable/of';
import { User } from 'src/app/dto/user.dto';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

import { UsersComponent } from './users.component';

describe('UsersComponent', () => {
  let component: UsersComponent;
  let fixture: ComponentFixture<UsersComponent>;
  const userService = {
    getUsers: jasmine.createSpy('getUsers'),
    lock: jasmine.createSpy('lock'),
    unlock: jasmine.createSpy('unlock'),
  };
  const authService = {
    getUserEmail: jasmine.createSpy('getUsers'),
  };

  const users: User[] = [
    {
      id: 1,
      firstName: 'Peter',
      lastName: 'Parker',
      email: 'admin@email.at',
      accountNonLocked: true,
    },
    {
      id: 2,
      firstName: 'Max',
      lastName: 'Mustermann',
      email: 'max@mustermann.at',
      accountNonLocked: true,
    },
    {
      id: 3,
      firstName: 'Viktor',
      lastName: 'Vergesslich',
      email: 'viktor@gmail.com',
      accountNonLocked: false,
    },
  ];

  beforeEach(async () => {
    userService.getUsers.and.returnValue(of(users));
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule,
        FormsModule,
        ToastrModule.forRoot(),
      ],
      providers: [
        {
          provide: UserService,
          useValue: userService,
        },
        {
          provide: AuthService,
          useValue: authService,
        },
      ],
      declarations: [UsersComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(UsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should list users', () => {
    const names = Array.from(
      fixture.nativeElement.querySelectorAll('.name')
    ) as HTMLElement[];
    const emails = Array.from(
      fixture.nativeElement.querySelectorAll('h6')
    ) as HTMLElement[];
    expect(emails[0]).toBeDefined();
    expect(names[0]).toBeDefined();
    expect(names[0].innerText).toEqual('Peter Parker');
    expect(emails[0].innerText).toEqual('admin@email.at');
    expect(names.length).toBe(3);
    expect(emails.length).toBe(3);
  });

  it('filters users with radio', () => {
    const radioLocked = fixture.nativeElement.querySelector(
      '#radioLocked'
    ) as HTMLElement;
    radioLocked.click();
    fixture.detectChanges();
    const names = Array.from(
      fixture.nativeElement.querySelectorAll('.name')
    ) as HTMLElement[];
    const emails = Array.from(
      fixture.nativeElement.querySelectorAll('h6')
    ) as HTMLElement[];
    expect(emails[0]).toBeDefined();
    expect(names[0]).toBeDefined();
    expect(names[0].innerText).toEqual('Viktor Vergesslich');
    expect(emails[0].innerText).toEqual('viktor@gmail.com');
    expect(names.length).toBe(1);
    expect(emails.length).toBe(1);
  });

  it('filters users with text field', () => {
    const filterUserInput = fixture.nativeElement.querySelector(
      '#filterUserInput'
    ) as HTMLInputElement;
    filterUserInput.value = 'admin';
    filterUserInput.dispatchEvent(new Event('input'));
    filterUserInput.dispatchEvent(new Event('keyup'));
    fixture.detectChanges();
    const names = Array.from(
      fixture.nativeElement.querySelectorAll('.name')
    ) as HTMLElement[];
    const emails = Array.from(
      fixture.nativeElement.querySelectorAll('h6')
    ) as HTMLElement[];
    expect(emails[0]).toBeDefined();
    expect(names[0]).toBeDefined();
    expect(names[0].innerText).toEqual('Peter Parker');
    expect(emails[0].innerText).toEqual('admin@email.at');
    expect(names.length).toBe(1);
    expect(emails.length).toBe(1);
  });

  it('calls lock when checkbox gets unchecked', () => {
    const accountNonLockedCheckboxes = Array.from(
      fixture.nativeElement.querySelectorAll('#accountNonLocked')
    ) as HTMLElement[];
    accountNonLockedCheckboxes[1].click();
    expect(userService.lock).toHaveBeenCalledWith(2);
  });

  it('calls unlock when checkbox gets checked', () => {
    const accountNonLockedCheckboxes = Array.from(
      fixture.nativeElement.querySelectorAll('#accountNonLocked')
    ) as HTMLElement[];
    accountNonLockedCheckboxes[2].click();
    expect(userService.unlock).toHaveBeenCalledWith(3);
  });
});

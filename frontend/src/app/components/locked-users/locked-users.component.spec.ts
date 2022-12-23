import {ComponentFixture, fakeAsync, TestBed, waitForAsync,} from '@angular/core/testing';

import {LockedUsersComponent} from './locked-users.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {UserService} from 'src/app/services/user.service';
import {AuthService} from 'src/app/services/auth.service';
import {User} from 'src/app/dto/user.dto';
import {of} from 'rxjs/internal/observable/of';

describe('LockedUsersComponent', () => {
  let component: LockedUsersComponent;
  let fixture: ComponentFixture<LockedUsersComponent>;
  const userService = {
    getLocked: jasmine.createSpy('getLocked'),
    unlock: jasmine.createSpy('unlock'),
  };
  const authService = {
    getUserRole: jasmine.createSpy('getUserRole'),
    isAdmin: jasmine.createSpy('isAdmin'),
  };
  let lockedUsers: User[];
  beforeEach(waitForAsync(() => {
    lockedUsers = [
      {
        email: 'viktor@test.com',
        firstName: 'Viktor',
        lastName: 'Vergesslich',
        id: 2,
      },
    ];
    userService.getLocked.and.returnValue(of(lockedUsers));
    userService.unlock.and.returnValue({
      subscribe: () => {
      }
    });
    authService.getUserRole.and.returnValue('ADMIN');
    authService.isAdmin.and.returnValue(true);
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule,
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
      declarations: [LockedUsersComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LockedUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('shows list of locked users', fakeAsync(() => {
    expect(component).toBeTruthy();
    expect(component.authService.isAdmin()).toEqual(true);
    const name = fixture.nativeElement.querySelector('.name') as HTMLElement;
    const email = fixture.nativeElement.querySelector('h6') as HTMLElement;
    expect(email).toBeDefined();
    expect(name).toBeDefined();
    expect(name.innerText).toEqual('Viktor Vergesslich');
    expect(email.innerText).toEqual('viktor@test.com');
  }));
  it('calls unlock', fakeAsync(() => {
    expect(component).toBeTruthy();
    console.log();
    expect(component.authService.isAdmin()).toEqual(true);
    const button = fixture.nativeElement.querySelector(
      '.users button'
    ) as HTMLElement;
    expect(button).toBeDefined();
    button.click();
    expect(userService.unlock).toHaveBeenCalledWith(2);
  }));
});

import {
  ComponentFixture,
  fakeAsync,
  flush,
  TestBed,
} from '@angular/core/testing';

import { ChangePasswordComponent } from './change-password.component';
import { UserService } from 'src/app/services/user.service';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

describe('ChangePasswordComponent', () => {
  let component: ChangePasswordComponent;
  let fixture: ComponentFixture<ChangePasswordComponent>;
  const userService = {
    changePassword: jasmine.createSpy('changePassword'),
  };

  beforeEach(async () => {
    const mockActivatedRoute = {
      queryParams: of({ token: '123' }),
    };

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        ReactiveFormsModule,
        ToastrModule.forRoot(),
      ],
      providers: [
        {
          provide: UserService,
          useValue: userService,
        },
        {
          provide: ActivatedRoute,
          useValue: mockActivatedRoute,
        },
      ],
      declarations: [ChangePasswordComponent],
    });
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call changePassword when form data is valid', fakeAsync(() => {
    TestBed.compileComponents();
    fixture = TestBed.createComponent(ChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component).toBeTruthy();
    expect(component.token).toEqual('123');

    const inputPassword = fixture.nativeElement.querySelector(
      '#inputPassword'
    ) as HTMLInputElement;
    const submitButton = fixture.nativeElement.querySelector(
      '#submitButton'
    ) as HTMLInputElement;
    expect(inputPassword).toBeDefined();
    expect(submitButton).toBeDefined();

    inputPassword.value = '12345678';
    inputPassword.dispatchEvent(new Event('input'));

    submitButton.click();

    expect(userService.changePassword).toHaveBeenCalledWith('12345678', '123');
  }));

  it('should not call changePassword when token is missing', fakeAsync(() => {
    const mockActivatedRoute = {
      queryParams: of({}),
    };
    TestBed.overrideProvider(ActivatedRoute, {
      useValue: mockActivatedRoute,
    });

    TestBed.compileComponents();
    fixture = TestBed.createComponent(ChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    userService.changePassword.calls.reset();

    const inputPassword = fixture.nativeElement.querySelector(
      '#inputPassword'
    ) as HTMLInputElement;
    const submitButton = fixture.nativeElement.querySelector(
      '#submitButton'
    ) as HTMLInputElement;
    expect(inputPassword).toBeDefined();
    expect(submitButton).toBeDefined();

    inputPassword.value = '12345678';
    inputPassword.dispatchEvent(new Event('input'));

    submitButton.click();

    expect(userService.changePassword).toHaveBeenCalledTimes(0);
    flush();
  }));

  it('should not call changePassword when password too short', fakeAsync(() => {
    const mockActivatedRoute = {
      queryParams: of({ token: '123' }),
    };
    TestBed.overrideProvider(ActivatedRoute, {
      useValue: mockActivatedRoute,
    });

    TestBed.compileComponents();
    fixture = TestBed.createComponent(ChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    userService.changePassword.calls.reset();

    const inputPassword = fixture.nativeElement.querySelector(
      '#inputPassword'
    ) as HTMLInputElement;
    const submitButton = fixture.nativeElement.querySelector(
      '#submitButton'
    ) as HTMLInputElement;
    expect(inputPassword).toBeDefined();
    expect(submitButton).toBeDefined();

    inputPassword.value = '1234567';
    inputPassword.dispatchEvent(new Event('input'));

    submitButton.click();

    expect(userService.changePassword).toHaveBeenCalledTimes(0);
  }));
});

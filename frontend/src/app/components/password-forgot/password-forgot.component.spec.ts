import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';
import { UserService } from 'src/app/services/user.service';

import { PasswordForgotComponent } from './password-forgot.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';

describe('PasswordForgotComponent', () => {
  let component: PasswordForgotComponent;
  let fixture: ComponentFixture<PasswordForgotComponent>;
  const userService = {
    resetPassword: jasmine.createSpy('resetPassword'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PasswordForgotComponent],
      imports: [ReactiveFormsModule, ToastrModule.forRoot()],
      providers: [
        {
          provide: UserService,
          useValue: userService,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PasswordForgotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call resetPassword when form data is valid', fakeAsync(() => {
    const inputEmail = fixture.nativeElement.querySelector(
      '#inputEmail'
    ) as HTMLInputElement;
    const submitButton = fixture.nativeElement.querySelector(
      '#submitButton'
    ) as HTMLInputElement;
    expect(inputEmail).toBeDefined();
    expect(submitButton).toBeDefined();

    inputEmail.value = 'test@test.com';
    inputEmail.dispatchEvent(new Event('input'));

    submitButton.click();

    expect(userService.resetPassword).toHaveBeenCalledWith('test@test.com');
  }));

  it('should not call resetPassword when email is missing', fakeAsync(() => {
    userService.resetPassword.calls.reset();
    const inputEmail = fixture.nativeElement.querySelector(
      '#inputEmail'
    ) as HTMLInputElement;
    const submitButton = fixture.nativeElement.querySelector(
      '#submitButton'
    ) as HTMLInputElement;
    expect(inputEmail).toBeDefined();
    expect(submitButton).toBeDefined();

    submitButton.click();

    expect(userService.resetPassword).toHaveBeenCalledTimes(0);
  }));
});

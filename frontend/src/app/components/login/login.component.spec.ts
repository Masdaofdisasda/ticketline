import {
  ComponentFixture,
  TestBed,
  waitForAsync,
  fakeAsync,
} from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthRequest } from 'src/app/dto/auth-request';
import { AuthService } from 'src/app/services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  const authService = {
    loginUser: jasmine.createSpy('loginUser'),
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule,
      ],
      providers: [
        {
          provide: AuthService,
          useValue: authService,
        },
      ],
      declarations: [LoginComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call loginUser when form data is valid', fakeAsync(() => {
    expect(component).toBeTruthy();
    const inputEmail = fixture.nativeElement.querySelector(
      '#inputEmail'
    ) as HTMLInputElement;
    const inputPassword = fixture.nativeElement.querySelector(
      '#inputPassword'
    ) as HTMLInputElement;
    const loginButton = fixture.nativeElement.querySelector(
      '#loginButton'
    ) as HTMLInputElement;
    expect(inputEmail).toBeDefined();
    expect(inputPassword).toBeDefined();
    expect(loginButton).toBeDefined();

    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '12345678';
    inputPassword.dispatchEvent(new Event('input'));

    loginButton.click();

    const expected = new AuthRequest('max@mustermann.at', '12345678');
    expect(authService.loginUser).toHaveBeenCalledWith(expected);
  }));
});

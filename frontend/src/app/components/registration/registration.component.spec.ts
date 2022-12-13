import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  waitForAsync,
} from '@angular/core/testing';

import { RegistrationComponent } from './registration.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RegistrationService } from 'src/app/services/registration.service';
import { RegistrationRequest } from 'src/app/dto/registration-request';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  const registrationService = {
    registerUser: jasmine.createSpy('registerUser'),
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
          provide: RegistrationService,
          useValue: registrationService,
        },
      ],
      declarations: [RegistrationComponent],
    }).compileComponents();
    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call registerUser when form data is valid', fakeAsync(() => {
    expect(component).toBeTruthy();
    const inputFirstName = fixture.nativeElement.querySelector(
      '#inputFirstName'
    ) as HTMLInputElement;
    const inputLastName = fixture.nativeElement.querySelector(
      '#inputLastName'
    ) as HTMLInputElement;
    const inputEmail = fixture.nativeElement.querySelector(
      '#inputEmail'
    ) as HTMLInputElement;
    const inputPassword = fixture.nativeElement.querySelector(
      '#inputPassword'
    ) as HTMLInputElement;
    const signUpButton = fixture.nativeElement.querySelector(
      '#signUpButton'
    ) as HTMLInputElement;
    const form = fixture.nativeElement.querySelector(
      '#registrationForm'
    ) as HTMLInputElement;
    expect(inputFirstName).toBeDefined();
    expect(inputLastName).toBeDefined();
    expect(inputEmail).toBeDefined();
    expect(inputPassword).toBeDefined();
    expect(signUpButton).toBeDefined();
    expect(form).toBeDefined();

    inputFirstName.value = 'Max';
    inputFirstName.dispatchEvent(new Event('input'));
    inputLastName.value = 'Mustermann';
    inputLastName.dispatchEvent(new Event('input'));
    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '12345678';
    inputPassword.dispatchEvent(new Event('input'));

    signUpButton.click();

    const expected = new RegistrationRequest(
      'max@mustermann.at',
      '12345678',
      'Max',
      'Mustermann'
    );
    expect(registrationService.registerUser).toHaveBeenCalledWith(expected);
  }));

  it('should not call registerUser when password length is less than 8', fakeAsync(() => {
    registrationService.registerUser.calls.reset();
    expect(component).toBeTruthy();
    const inputFirstName = fixture.nativeElement.querySelector(
      '#inputFirstName'
    ) as HTMLInputElement;
    const inputLastName = fixture.nativeElement.querySelector(
      '#inputLastName'
    ) as HTMLInputElement;
    const inputEmail = fixture.nativeElement.querySelector(
      '#inputEmail'
    ) as HTMLInputElement;
    const inputPassword = fixture.nativeElement.querySelector(
      '#inputPassword'
    ) as HTMLInputElement;
    const signUpButton = fixture.nativeElement.querySelector(
      '#signUpButton'
    ) as HTMLInputElement;
    const form = fixture.nativeElement.querySelector(
      '#registrationForm'
    ) as HTMLInputElement;
    expect(inputFirstName).toBeDefined();
    expect(inputLastName).toBeDefined();
    expect(inputEmail).toBeDefined();
    expect(inputPassword).toBeDefined();
    expect(signUpButton).toBeDefined();
    expect(form).toBeDefined();

    inputFirstName.value = 'Max';
    inputFirstName.dispatchEvent(new Event('input'));
    inputLastName.value = 'Mustermann';
    inputLastName.dispatchEvent(new Event('input'));
    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '1234567';
    inputPassword.dispatchEvent(new Event('input'));

    signUpButton.click();

    expect(registrationService.registerUser).toHaveBeenCalledTimes(0);
  }));

  it('should not call registerUser when attribute is missing', fakeAsync(() => {
    registrationService.registerUser.calls.reset();
    expect(component).toBeTruthy();
    const inputFirstName = fixture.nativeElement.querySelector(
      '#inputFirstName'
    ) as HTMLInputElement;
    const inputLastName = fixture.nativeElement.querySelector(
      '#inputLastName'
    ) as HTMLInputElement;
    const inputEmail = fixture.nativeElement.querySelector(
      '#inputEmail'
    ) as HTMLInputElement;
    const inputPassword = fixture.nativeElement.querySelector(
      '#inputPassword'
    ) as HTMLInputElement;
    const signUpButton = fixture.nativeElement.querySelector(
      '#signUpButton'
    ) as HTMLInputElement;
    const form = fixture.nativeElement.querySelector(
      '#registrationForm'
    ) as HTMLInputElement;
    expect(inputFirstName).toBeDefined();
    expect(inputLastName).toBeDefined();
    expect(inputEmail).toBeDefined();
    expect(inputPassword).toBeDefined();
    expect(signUpButton).toBeDefined();
    expect(form).toBeDefined();

    inputFirstName.value = '';
    inputFirstName.dispatchEvent(new Event('input'));
    inputLastName.value = 'Mustermann';
    inputLastName.dispatchEvent(new Event('input'));
    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '12345678';
    inputPassword.dispatchEvent(new Event('input'));

    signUpButton.click();

    inputFirstName.value = 'Max';
    inputFirstName.dispatchEvent(new Event('input'));
    inputLastName.value = '';
    inputLastName.dispatchEvent(new Event('input'));

    signUpButton.click();

    inputLastName.value = 'Mustermann';
    inputLastName.dispatchEvent(new Event('input'));
    inputEmail.value = '';
    inputEmail.dispatchEvent(new Event('input'));

    signUpButton.click();

    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '';
    inputPassword.dispatchEvent(new Event('input'));

    signUpButton.click();

    expect(registrationService.registerUser).toHaveBeenCalledTimes(0);
  }));
});

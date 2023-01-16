import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { CreateUserRequest } from 'src/app/dto/create-user-request.dto';
import { UserService } from 'src/app/services/user.service';
import { ToastrModule } from 'ngx-toastr';

import { CreateUserComponent } from './create-user.component';

describe('CreateUserComponent', () => {
  let component: CreateUserComponent;
  let fixture: ComponentFixture<CreateUserComponent>;
  const userService = {
    createUser: jasmine.createSpy('userService'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ReactiveFormsModule,
        ToastrModule.forRoot(),
      ],
      providers: [
        {
          provide: UserService,
          useValue: userService,
        },
      ],
      declarations: [CreateUserComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call createUser when form data is valid', fakeAsync(() => {
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
    const createButton = fixture.nativeElement.querySelector(
      '#createButton'
    ) as HTMLInputElement;
    const form = fixture.nativeElement.querySelector(
      '#createUserForm'
    ) as HTMLInputElement;
    expect(inputFirstName).toBeDefined();
    expect(inputLastName).toBeDefined();
    expect(inputEmail).toBeDefined();
    expect(inputPassword).toBeDefined();
    expect(createButton).toBeDefined();
    expect(form).toBeDefined();

    inputFirstName.value = 'Max';
    inputFirstName.dispatchEvent(new Event('input'));
    inputLastName.value = 'Mustermann';
    inputLastName.dispatchEvent(new Event('input'));
    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '12345678';
    inputPassword.dispatchEvent(new Event('input'));

    createButton.click();

    const expected = new CreateUserRequest(
      'max@mustermann.at',
      '12345678',
      'Max',
      'Mustermann',
      false
    );
    expect(userService.createUser).toHaveBeenCalledWith(expected);
  }));

  it('should not call createUser when password length is less than 8', fakeAsync(() => {
    userService.createUser.calls.reset();
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
    const createButton = fixture.nativeElement.querySelector(
      '#createButton'
    ) as HTMLInputElement;
    const form = fixture.nativeElement.querySelector(
      '#registrationForm'
    ) as HTMLInputElement;
    expect(inputFirstName).toBeDefined();
    expect(inputLastName).toBeDefined();
    expect(inputEmail).toBeDefined();
    expect(inputPassword).toBeDefined();
    expect(createButton).toBeDefined();
    expect(form).toBeDefined();

    inputFirstName.value = 'Max';
    inputFirstName.dispatchEvent(new Event('input'));
    inputLastName.value = 'Mustermann';
    inputLastName.dispatchEvent(new Event('input'));
    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '1234567';
    inputPassword.dispatchEvent(new Event('input'));

    createButton.click();

    expect(userService.createUser).toHaveBeenCalledTimes(0);
  }));

  it('should not call createUser when attribute is missing', fakeAsync(() => {
    userService.createUser.calls.reset();
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
    const createButton = fixture.nativeElement.querySelector(
      '#createButton'
    ) as HTMLInputElement;
    const form = fixture.nativeElement.querySelector(
      '#registrationForm'
    ) as HTMLInputElement;
    expect(inputFirstName).toBeDefined();
    expect(inputLastName).toBeDefined();
    expect(inputEmail).toBeDefined();
    expect(inputPassword).toBeDefined();
    expect(createButton).toBeDefined();
    expect(form).toBeDefined();

    inputFirstName.value = '';
    inputFirstName.dispatchEvent(new Event('input'));
    inputLastName.value = 'Mustermann';
    inputLastName.dispatchEvent(new Event('input'));
    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '12345678';
    inputPassword.dispatchEvent(new Event('input'));

    createButton.click();

    inputFirstName.value = 'Max';
    inputFirstName.dispatchEvent(new Event('input'));
    inputLastName.value = '';
    inputLastName.dispatchEvent(new Event('input'));

    createButton.click();

    inputLastName.value = 'Mustermann';
    inputLastName.dispatchEvent(new Event('input'));
    inputEmail.value = '';
    inputEmail.dispatchEvent(new Event('input'));

    createButton.click();

    inputEmail.value = 'max@mustermann.at';
    inputEmail.dispatchEvent(new Event('input'));
    inputPassword.value = '';
    inputPassword.dispatchEvent(new Event('input'));

    createButton.click();

    expect(userService.createUser).toHaveBeenCalledTimes(0);
  }));
});

import {ComponentFixture, fakeAsync, flush, TestBed} from '@angular/core/testing';

import { ArtistCreateComponent } from './artist-create.component';
import { Router } from '@angular/router';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ArtistService } from '../../../services/artist.service';
import { EventService } from '../../../services/event.service';
import { of } from 'rxjs/internal/observable/of';
import { ArtistDto } from '../../../dto/artist.dto';
import {HttpClientModule} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('ArtistCreateComponent', () => {
  let component: ArtistCreateComponent;
  let fixture: ComponentFixture<ArtistCreateComponent>;
  let router: Router;
  const artistService = {
    create: jasmine.createSpy('create'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArtistCreateComponent],
      imports: [
        FormsModule,
        ReactiveFormsModule,
        ToastrModule.forRoot(),
        NgbModule,
        HttpClientTestingModule,
        HttpClientModule,
        BrowserAnimationsModule,
        RouterTestingModule.withRoutes([]),
      ],
      providers: [
        {
          provide: ArtistService,
          useValue: artistService,
          formBuilder: FormBuilder,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ArtistCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.get(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onSubmit should call service', fakeAsync(() => {
    artistService.create.and.returnValue(
      of({
        name: 'ArtistName',
      } as ArtistDto)
    );

    const navigationSpy = spyOn(router, 'navigate');

    component.createFormGroup.get('artistName').setValue('ArtistName');

    component.onSubmit();

    expect(artistService.create).toHaveBeenCalledWith({
      name: 'ArtistName',
    });

    expect(navigationSpy).toHaveBeenCalledWith(['/artist']);
    flush();
  }));
});

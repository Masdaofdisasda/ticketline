import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowVenueComponent} from './show-venue.component';

describe('ShowVenueComponent', () => {
  let component: ShowVenueComponent;
  let fixture: ComponentFixture<ShowVenueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowVenueComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShowVenueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

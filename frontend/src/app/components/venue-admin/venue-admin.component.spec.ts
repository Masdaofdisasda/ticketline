import {ComponentFixture, TestBed} from '@angular/core/testing';

import {VenueAdminComponent} from './venue-admin.component';

describe('VenueAdminComponent', () => {
  let component: VenueAdminComponent;
  let fixture: ComponentFixture<VenueAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VenueAdminComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(VenueAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

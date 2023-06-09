import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomPlanComponent } from './room-plan.component';

describe('RoomPlanComponent', () => {
  let component: RoomPlanComponent;
  let fixture: ComponentFixture<RoomPlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoomPlanComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoomPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

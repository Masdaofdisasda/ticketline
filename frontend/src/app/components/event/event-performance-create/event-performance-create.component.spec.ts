import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventPerformanceCreateComponent } from './event-performance-create.component';

describe('EventPerformanceCreateComponent', () => {
  let component: EventPerformanceCreateComponent;
  let fixture: ComponentFixture<EventPerformanceCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventPerformanceCreateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventPerformanceCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

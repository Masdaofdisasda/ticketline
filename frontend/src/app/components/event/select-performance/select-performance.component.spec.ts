import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectPerformanceComponent } from './select-performance.component';

describe('SelectPerformanceComponent', () => {
  let component: SelectPerformanceComponent;
  let fixture: ComponentFixture<SelectPerformanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SelectPerformanceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectPerformanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

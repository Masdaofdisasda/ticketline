import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ColorInputFieldComponent } from './color-input-field.component';

describe('ColorInputFieldComponent', () => {
  let component: ColorInputFieldComponent;
  let fixture: ComponentFixture<ColorInputFieldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ColorInputFieldComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ColorInputFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

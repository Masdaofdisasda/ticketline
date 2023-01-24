import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PriceCategoryDropdownComponent} from './price-category-dropdown.component';

describe('PriceCategoryDropdownComponent', () => {
  let component: PriceCategoryDropdownComponent;
  let fixture: ComponentFixture<PriceCategoryDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PriceCategoryDropdownComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PriceCategoryDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

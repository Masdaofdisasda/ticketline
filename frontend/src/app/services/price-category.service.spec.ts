import { TestBed } from '@angular/core/testing';

import { PriceCategoryService } from './price-category.service';

describe('PriceCategoryService', () => {
  let service: PriceCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PriceCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

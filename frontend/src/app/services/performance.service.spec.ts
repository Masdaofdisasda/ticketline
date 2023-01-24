import {TestBed} from '@angular/core/testing';

import {PerformanceService} from './performance.service';
import {HttpClientModule} from '@angular/common/http';

describe('PerofrmanceService', () => {
  let service: PerformanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule]
    });
    service = TestBed.inject(PerformanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { VacuumService } from './vacuum.service';

describe('VacuumService', () => {
  let service: VacuumService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VacuumService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

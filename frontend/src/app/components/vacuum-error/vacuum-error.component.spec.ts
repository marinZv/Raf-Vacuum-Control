import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacuumErrorComponent } from './vacuum-error.component';

describe('VacuumErrorComponent', () => {
  let component: VacuumErrorComponent;
  let fixture: ComponentFixture<VacuumErrorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VacuumErrorComponent]
    });
    fixture = TestBed.createComponent(VacuumErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

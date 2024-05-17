import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVacuumsComponent } from './add-vacuums.component';

describe('AddVacuumsComponent', () => {
  let component: AddVacuumsComponent;
  let fixture: ComponentFixture<AddVacuumsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddVacuumsComponent]
    });
    fixture = TestBed.createComponent(AddVacuumsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

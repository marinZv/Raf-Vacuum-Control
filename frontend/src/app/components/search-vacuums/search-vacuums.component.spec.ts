import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchVacuumsComponent } from './search-vacuums.component';

describe('SearchVacuumsComponent', () => {
  let component: SearchVacuumsComponent;
  let fixture: ComponentFixture<SearchVacuumsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchVacuumsComponent]
    });
    fixture = TestBed.createComponent(SearchVacuumsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

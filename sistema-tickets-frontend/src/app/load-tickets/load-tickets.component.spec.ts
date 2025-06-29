import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadTicketsComponent } from './load-tickets.component';

describe('LoadTicketsComponent', () => {
  let component: LoadTicketsComponent;
  let fixture: ComponentFixture<LoadTicketsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoadTicketsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoadTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

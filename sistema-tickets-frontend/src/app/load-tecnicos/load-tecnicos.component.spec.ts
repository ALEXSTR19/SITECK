import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadTecnicosComponent } from './load-tecnicos.component';

describe('LoadTecnicosComponent', () => {
  let component: LoadTecnicosComponent;
  let fixture: ComponentFixture<LoadTecnicosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoadTecnicosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoadTecnicosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

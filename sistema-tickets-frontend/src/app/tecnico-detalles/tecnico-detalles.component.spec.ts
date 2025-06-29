import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TecnicoDetallesComponent } from './tecnico-detalles.component';

describe('TecnicoDetallesComponent', () => {
  let component: TecnicoDetallesComponent;
  let fixture: ComponentFixture<TecnicoDetallesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TecnicoDetallesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TecnicoDetallesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EditTecnicoComponent } from './edit-tecnico.component';

describe('EditTecnicoComponent', () => {
  let component: EditTecnicoComponent;
  let fixture: ComponentFixture<EditTecnicoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditTecnicoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditTecnicoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

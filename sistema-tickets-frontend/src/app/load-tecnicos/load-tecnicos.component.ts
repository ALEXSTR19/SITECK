import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TecnicosService } from '../services/tecnicos.service';

@Component({
  selector: 'app-load-tecnicos',
  templateUrl: './load-tecnicos.component.html'
})
export class LoadTecnicosComponent implements OnInit {

  tecnicoForm!: FormGroup;

  constructor(private fb: FormBuilder, private tecnicosService: TecnicosService) {}

  ngOnInit(): void {
    this.tecnicoForm = this.fb.group({
      nombre: ['', Validators.required],
      especialidad: ['', Validators.required]
    });


    });
  }

  guardarTecnico(): void {
    if (!this.tecnicoForm || this.tecnicoForm.invalid) {
      return;
    }

    this.tecnicosService.crearTecnico(this.tecnicoForm.value).subscribe({
      next: (resp) => {
        this.tecnicoForm.reset();
        // Mostrar mensaje de éxito si quieres
      },
      error: (err) => {
        console.error('Error al guardar técnico:', err);
      }
    });
  }
}

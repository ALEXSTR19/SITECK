import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TecnicosService } from '../services/tecnicos.service';
import { ServiciosService } from '../services/servicios.service';
import { Servicio } from '../models/servicio.model';

@Component({
  selector: 'app-load-tecnicos',
  templateUrl: './load-tecnicos.component.html'
})
export class LoadTecnicosComponent implements OnInit {

  tecnicoForm!: FormGroup;
  servicios: Servicio[] = [];

  constructor(private fb: FormBuilder, private tecnicosService: TecnicosService, private servicioServices: ServiciosService) {}

  ngOnInit(): void {
    this.tecnicoForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      codigo: [''],
      especialidades: [[], Validators.required]
    });

    this.tecnicosService.obtenerProximoCodigo().subscribe(code => {
      this.tecnicoForm.patchValue({ codigo: code });
    });

    this.servicioServices.getServicios().subscribe({
      next: value => (this.servicios = value),
      error: err => console.error('Error al obtener servicios', err),
    });
  }

  guardarTecnico(): void {
    if (!this.tecnicoForm || this.tecnicoForm.invalid) {
      return;
    }

    this.tecnicosService.crearTecnico(this.tecnicoForm.value).subscribe({
      next: (resp) => {
        this.tecnicoForm.reset();
        this.tecnicosService.obtenerProximoCodigo().subscribe(code => {
          this.tecnicoForm.patchValue({ codigo: code });
        });
      },
      error: (err) => {
        console.error('Error al guardar técnico:', err);
      }
    });
  }
}

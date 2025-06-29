import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TecnicosService } from '../services/tecnicos.service';
import { ServiciosService } from '../services/servicios.service';
import { Servicio } from '../models/servicio.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-load-tecnicos',
  templateUrl: './load-tecnicos.component.html',
  styleUrls: ['./load-tecnicos.component.css']
})
export class LoadTecnicosComponent implements OnInit {
  tecnicoForm!: FormGroup;
  servicios: Servicio[] = [];

  constructor(private fb: FormBuilder,
              private tecnicosService: TecnicosService,
              private serviciosService: ServiciosService) {}

  ngOnInit(): void {
    this.tecnicoForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      codigo: ['', Validators.required],
      especialidad: ['', Validators.required]
    });

    this.serviciosService.getServicios().subscribe({
      next: value => this.servicios = value,
      error: err => console.error('Error al obtener servicios', err)
    });
  }

  guardarTecnico() {
    if (this.tecnicoForm.invalid) {
      return;
    }
    this.tecnicosService.crearTecnico(this.tecnicoForm.value).subscribe({
      next: () => {
        Swal.fire('Técnico guardado', 'El técnico se ha registrado correctamente', 'success');
        this.tecnicoForm.reset();
      },
      error: () => {
        Swal.fire('Error', 'No se pudo guardar el técnico', 'error');
      }
    });
  }
}

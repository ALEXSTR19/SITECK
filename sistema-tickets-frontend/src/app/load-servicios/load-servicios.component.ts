import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ServiciosService } from '../services/servicios.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-load-servicios',
  templateUrl: './load-servicios.component.html',
  styleUrls: ['./load-servicios.component.css']
})
export class LoadServiciosComponent implements OnInit {
  servicioForm!: FormGroup;

  constructor(private fb: FormBuilder, private serviciosService: ServiciosService) {}

  ngOnInit(): void {
    this.servicioForm = this.fb.group({
      nombre: ['', Validators.required],
      liderCodigo: ['']
    });
  }

  guardarServicio() {
    if (this.servicioForm.invalid) {
      return;
    }
    this.serviciosService.crearServicio(this.servicioForm.value).subscribe({
      next: () => {
        Swal.fire('Servicio guardado', 'El servicio se ha registrado correctamente', 'success');
        this.servicioForm.reset();
      },
      error: () => {
        Swal.fire('Error', 'No se pudo guardar el servicio', 'error');
      }
    });
  }
}

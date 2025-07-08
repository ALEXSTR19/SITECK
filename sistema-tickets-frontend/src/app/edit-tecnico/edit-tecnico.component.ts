import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TecnicosService } from '../services/tecnicos.service';
import { ServiciosService } from '../services/servicios.service';
import { Servicio } from '../models/servicio.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-tecnico',
  templateUrl: './edit-tecnico.component.html',
})
export class EditTecnicoComponent implements OnInit {
  tecnicoForm!: FormGroup;
  servicios: Servicio[] = [];
  codigo!: string;

  constructor(
    private fb: FormBuilder,
    private tecnicosService: TecnicosService,
    private serviciosService: ServiciosService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.tecnicoForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      username: [''],
      password: [''],
      codigo: [''],
      especialidades: [[], Validators.required]
    });

    this.serviciosService.getServicios().subscribe({
      next: s => (this.servicios = s),
      error: err => console.error('Error al obtener servicios', err)
    });

    this.route.params.subscribe(params => {
      this.codigo = params['codigo'];
      this.tecnicosService.getTecnicoPorCodigo(this.codigo).subscribe({
        next: t => {
          this.tecnicoForm.patchValue({
            nombre: t.nombre,
            apellido: t.apellido,
            email: t.email,
            codigo: t.codigo,
            username: t.username,
            especialidades: t.especialidades
          });
        },
        error: err => console.error('Error al cargar técnico', err)
      });
    });
  }

  guardarCambios() {
    if (this.tecnicoForm.invalid) {
      return;
    }
    this.tecnicosService.editarTecnico(this.codigo, this.tecnicoForm.value).subscribe({
      next: () => {
        Swal.fire('Técnico actualizado', 'El técnico se actualizó correctamente', 'success');
        this.router.navigate(['/admin/tecnicos']);
      },
      error: () => {
        Swal.fire('Error', 'No se pudo actualizar el técnico', 'error');
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TecnicosService } from '../services/tecnicos.service';
import { ServiciosService } from '../services/servicios.service';
import { Servicio } from '../models/servicio.model';
import { AuthService } from '../services/auth.service';
import { Usuario } from '../models/usuario.model';

@Component({
  selector: 'app-load-tecnicos',
  templateUrl: './load-tecnicos.component.html'
})
export class LoadTecnicosComponent implements OnInit {

  tecnicoForm!: FormGroup;
  servicios: Servicio[] = [];

  constructor(
    private fb: FormBuilder,
    private tecnicosService: TecnicosService,
    private servicioServices: ServiciosService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.tecnicoForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      codigo: [''],
      especialidad: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required]
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
    const { nombre, apellido, codigo, especialidad, username, password } = this.tecnicoForm.value;
    const tecnico = { nombre, apellido, codigo, especialidad } as any;

    this.tecnicosService.crearTecnico(tecnico).subscribe({
      next: () => {
        const usuario: Usuario = {
          username,
          password,
          servicios: especialidad,
          role: 'TECNICO'
        };
        this.authService.register(usuario).subscribe({
          error: err => console.error('Error al registrar usuario:', err)
        });
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

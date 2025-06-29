import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { Usuario } from '../models/usuario.model';
import { ServiciosService } from '../services/servicios.service';
import { Servicio } from '../models/servicio.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  servicios: Servicio[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private serviciosService: ServiciosService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      servicios: [[], Validators.required],
      role: ['TECNICO', Validators.required]
    });
  }

  ngOnInit(): void {
    this.serviciosService.getServicios().subscribe({
      next: (data) => (this.servicios = data),
      error: () => console.error('No se pudieron cargar los servicios')
    });
  }

  register() {
    const formValue = this.registerForm.value;
    const usuario: Usuario = {
      username: formValue.username,
      password: formValue.password,
      servicios: (formValue.servicios as string[]).join(','),
      role: formValue.role
    };
    this.authService.register(usuario).subscribe({
      next: () => this.router.navigateByUrl('/login'),
      error: () => alert('No se pudo registrar')
    });
  }
}

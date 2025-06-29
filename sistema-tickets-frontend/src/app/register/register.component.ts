import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { Usuario } from '../models/usuario.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      role: ['TECNICO', Validators.required]
    });
  }

  register() {
    const usuario: Usuario = this.registerForm.value;
    this.authService.register(usuario).subscribe({
      next: () => this.router.navigateByUrl('/login'),
      error: () => alert('No se pudo registrar')
    });
  }
}

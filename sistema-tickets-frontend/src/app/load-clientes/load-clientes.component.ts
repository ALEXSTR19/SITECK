import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientesService } from '../services/clientes.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-load-clientes',
  templateUrl: './load-clientes.component.html'
})
export class LoadClientesComponent implements OnInit {
  clienteForm!: FormGroup;

  constructor(private fb: FormBuilder, private clientesService: ClientesService) {}

  ngOnInit(): void {
    this.clienteForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required]
    });
  }

  guardarCliente() {
    if (this.clienteForm.invalid) {
      return;
    }
    this.clientesService.crearCliente(this.clienteForm.value).subscribe({
      next: () => {
        Swal.fire('Cliente guardado', 'El cliente se registró correctamente', 'success');
        this.clienteForm.reset();
      },
      error: () => {
        Swal.fire('Error', 'No se pudo guardar el cliente', 'error');
      }
    });
  }
}

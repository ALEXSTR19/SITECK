import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Tecnico } from '../models/tecnicos.model';
import { TecnicosService } from '../services/tecnicos.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-tecnicos',
  templateUrl: './tecnicos.component.html',
  styleUrls: ['./tecnicos.component.css']
})
export class TecnicosComponent implements OnInit {

  tecnicos!: Array<Tecnico>;
  tecnicosDataSource!: MatTableDataSource<Tecnico>;
  displayedColumns: string[] = ['id', 'nombre', 'apellido', 'codigo', 'especialidades','tickets','acciones'];

  constructor(private tecnicoService: TecnicosService, private router: Router) { }

  ngOnInit(): void {
    this.tecnicoService.getAllTecnicos().subscribe({
      next: value => {
        this.tecnicos = value;
        this.tecnicosDataSource = new MatTableDataSource<Tecnico>(this.tecnicos);
      },
      error: err => {
        console.error('Error al obtener los técnicos', err);
      }
    });
  }

  listarTicketsDeTecnico(tecnico: Tecnico) {
    this.router.navigateByUrl(`/admin/tecnico-detalles/${tecnico.codigo}`);
  }

  editarTecnico(tecnico: Tecnico) {
    this.router.navigate(['/admin/edit-tecnico', tecnico.codigo]);
  }

  eliminarTecnico(tecnico: Tecnico) {
    Swal.fire({
      title: '¿Eliminar técnico?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar'
    }).then(result => {
      if (result.isConfirmed) {
        this.tecnicoService.eliminarTecnico(tecnico.codigo).subscribe({
          next: () => {
            this.tecnicos = this.tecnicos.filter(t => t.codigo !== tecnico.codigo);
            this.tecnicosDataSource.data = this.tecnicos;
            Swal.fire('Eliminado', 'El técnico ha sido eliminado', 'success');
          },
          error: () => {
            Swal.fire('Error', 'No se pudo eliminar el técnico', 'error');
          }
        });
      }
    });
  }

  getServiciosNombres(tecnico: Tecnico): string {
    return tecnico.especialidades?.map(e => e.nombre).join(', ') ?? '';
  }

}

import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Tecnico } from '../models/tecnicos.model';
import { TecnicosService } from '../services/tecnicos.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tecnicos',
  templateUrl: './tecnicos.component.html',
  styleUrls: ['./tecnicos.component.css']
})
export class TecnicosComponent implements OnInit {

  tecnicos!: Array<Tecnico>;
  tecnicosDataSource!: MatTableDataSource<Tecnico>;
  displayedColumns: string[] = ['id', 'nombre', 'apellido', 'codigo', 'especialidades','tickets'];

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

  getServiciosNombres(tecnico: Tecnico): string {
    return tecnico.especialidades?.map(e => e.nombre).join(', ') ?? '';
  }

}

import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Cliente } from '../models/cliente.model';
import { ClientesService } from '../services/clientes.service';

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html',
  styleUrls: ['./clientes.component.css']
})
export class ClientesComponent implements OnInit {

  clientes: Cliente[] = [];
  dataSource!: MatTableDataSource<Cliente>;
  displayedColumns: string[] = ['id', 'nombre', 'apellido'];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private clientesService: ClientesService) {}

  ngOnInit(): void {
    this.clientesService.getClientes().subscribe({
      next: data => {
        this.clientes = data;
        this.dataSource = new MatTableDataSource(this.clientes);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: err => console.error('Error al cargar clientes', err)
    });
  }
}

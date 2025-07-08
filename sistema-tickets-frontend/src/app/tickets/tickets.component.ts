import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { TecnicosService } from '../services/tecnicos.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tickets',
  templateUrl: './tickets.component.html',
  styleUrls: ['./tickets.component.css']
})
export class TicketsComponent implements OnInit {
public tickets: any;
  public dataSource: any;

  public displayedColumns = ['id', 'fecha', 'cantidad', 'type', 'status', 'priority', 'nombre', 'info1', 'info2', 'info3', 'pagado', 'reporte', 'acciones'];

  /*@ViewChild es un decorador que permite acceder a un componente hijo del DOM */
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private http: HttpClient, private tecnicoService: TecnicosService, private router: Router) { }
  ngOnInit(): void {
    this.tecnicoService.getAllTickets().subscribe({
      next: data => {
        this.tickets = data;
        this.dataSource = new MatTableDataSource(this.tickets);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: err => {
        console.error('Error al obtener los tickets', err);
      }
    })
  }

  verDetalles(id: number){
    this.router.navigate(['/admin/ticket-details', id]);
  }
}

import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { TecnicosService } from '../services/tecnicos.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-tickets',
  templateUrl: './tickets.component.html',
  styleUrls: ['./tickets.component.css']
})
export class TicketsComponent implements OnInit {
public tickets: any;
  public dataSource: any;

  public displayedColumns = ['id', 'fecha', 'horaVisita', 'status', 'priority', 'nombre', 'info1', 'info2', 'info3', 'pagado', 'reporte', 'acciones'];

  /*@ViewChild es un decorador que permite acceder a un componente hijo del DOM */
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private http: HttpClient, private tecnicoService: TecnicosService, private router: Router, private route: ActivatedRoute) { }
  ngOnInit(): void {
    const filter = this.route.snapshot.queryParamMap.get('filter');
    this.tecnicoService.getAllTickets().subscribe({
      next: data => {
        this.tickets = data;
        this.applyFilter(filter);
      },
      error: err => {
        console.error('Error al obtener los tickets', err);
      }
    });
  }

  private applyFilter(filter: string | null) {
    let filtered = this.tickets;
    const today = new Date().toISOString().slice(0,10);
    switch(filter) {
      case 'paid':
        filtered = this.tickets.filter((t: any) => t.pagado);
        break;
      case 'unpaid':
        filtered = this.tickets.filter((t: any) => !t.pagado);
        break;
      case 'finished-unpaid':
        filtered = this.tickets.filter((t: any) => t.status === 'FINALIZADO' && !t.pagado);
        break;
      case 'finished-paid':
        filtered = this.tickets.filter((t: any) => t.status === 'FINALIZADO' && t.pagado);
        break;
      case 'today':
        filtered = this.tickets.filter((t: any) => ('' + t.fecha).startsWith(today));
        break;
      case 'pending':
        filtered = this.tickets.filter((t: any) => t.status === 'PENDIENTE');
        break;
      case 'in-progress':
        filtered = this.tickets.filter((t: any) => t.status === 'EN_PROCESO');
        break;
      case 'finished':
        filtered = this.tickets.filter((t: any) => t.status === 'FINALIZADO');
        break;
    }
    this.dataSource = new MatTableDataSource(filtered);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  verDetalles(id: number){
    this.router.navigate(['/admin/ticket-details', id]);
  }
}

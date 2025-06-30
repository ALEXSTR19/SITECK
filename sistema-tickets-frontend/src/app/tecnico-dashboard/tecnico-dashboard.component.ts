import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { MatTableDataSource } from '@angular/material/table';
import { Ticket } from '../models/tecnicos.model';
import { TecnicosService } from '../services/tecnicos.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-tecnico-dashboard',
  templateUrl: './tecnico-dashboard.component.html',
  styleUrls: ['./tecnico-dashboard.component.css']
})
export class TecnicoDashboardComponent implements OnInit {
  tickets: Ticket[] = [];
  todayTickets: Ticket[] = [];
  pendingTickets: Ticket[] = [];
  completedTickets: Ticket[] = [];
  tecnicoCodigo: string | undefined;

  todayDataSource = new MatTableDataSource<Ticket>();
  pendingDataSource = new MatTableDataSource<Ticket>();
  completedDataSource = new MatTableDataSource<Ticket>();
  allDataSource = new MatTableDataSource<Ticket>();

  displayedColumns: string[] = ['id', 'fecha', 'cantidad', 'type', 'status', 'nombre'];

  activeCategory = '';
  statusChart?: Chart;

  constructor(private tecnicosService: TecnicosService, private authService: AuthService) {}

  ngOnInit(): void {
    this.tecnicoCodigo = this.authService.username;
    if (!this.tecnicoCodigo) {
      return;
    }
    this.tecnicosService.getTicketsDeTecnico(this.tecnicoCodigo).subscribe({
      next: (data) => {
        this.tickets = data;
        this.filterTickets();
      },
      error: (err) => {
        console.error('Error al cargar los tickets', err);
      }
    });
  }

  filterTickets() {
    const todayStr = new Date().toISOString().split('T')[0];
    this.todayTickets = this.tickets.filter(t => new Date(t.fecha).toISOString().split('T')[0] === todayStr);
    this.pendingTickets = this.tickets.filter(t => t.status === 'PENDIENTE' || t.status === 'EN_PROCESO');
    this.completedTickets = this.tickets.filter(t => t.status === 'FINALIZADO');

    this.todayDataSource.data = this.todayTickets;
    this.pendingDataSource.data = this.pendingTickets;
    this.completedDataSource.data = this.completedTickets;
    this.allDataSource.data = this.tickets;
  }

  initializeChart() {
    const counts = {
      pendiente: this.tickets.filter(t => t.status === 'PENDIENTE').length,
      enProceso: this.tickets.filter(t => t.status === 'EN_PROCESO').length,
      finalizado: this.tickets.filter(t => t.status === 'FINALIZADO').length,
      cancelado: this.tickets.filter(t => t.status === 'CANCELADO').length
    };

    if (this.statusChart) {
      this.statusChart.destroy();
    }

    this.statusChart = new Chart('statusChart', {
      type: 'doughnut',
      data: {
        labels: ['Pendiente', 'En proceso', 'Finalizado', 'Cancelado'],
        datasets: [{
          data: [
            counts.pendiente,
            counts.enProceso,
            counts.finalizado,
            counts.cancelado
          ],
          backgroundColor: ['#FFC107', '#2196F3', '#4CAF50', '#F44336']
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }

  setCategory(cat: string) {
    this.activeCategory = this.activeCategory === cat ? '' : cat;
    if (this.activeCategory === 'metrics') {
      setTimeout(() => this.initializeChart());
    }
  }
}

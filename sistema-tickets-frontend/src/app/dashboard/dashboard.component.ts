import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { DashboardService } from '../services/dashboard.service';
import { TicketStat } from '../models/ticket-stat.model';
import { AdminStats } from '../models/admin-stats.model';
import { Rating } from '../models/rating.model';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  dayChart?: Chart;
  weekChart?: Chart;
  monthChart?: Chart;
  serviciosChart?: Chart;
  clientesChart?: Chart;
  tecnicosChart?: Chart;
  adminStats?: AdminStats;
  ratingsDataSource = new MatTableDataSource<Rating>();
  ratingColumns: string[] = ['ticket','tecnico','fecha','stars','comment'];

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getAdminStats().subscribe(s => this.adminStats = s);
    this.dashboardService.getStatsByDay().subscribe(d => this.createChart('dayChart', d));
    this.dashboardService.getStatsByWeek().subscribe(d => this.createChart('weekChart', d));
    this.dashboardService.getStatsByMonth().subscribe(d => this.createChart('monthChart', d));
    this.dashboardService.getTopServicios().subscribe(d => this.createChart('serviciosChart', d));
    this.dashboardService.getTopClientes().subscribe(d => this.createChart('clientesChart', d));
    this.dashboardService.getTopTecnicos().subscribe(d => this.createChart('tecnicosChart', d));
    this.dashboardService.getRatings().subscribe(r => this.ratingsDataSource.data = r);
  }

  private createChart(elementId: string, stats: TicketStat[]) {
    const labels = stats.map(s => s.label);
    const data = stats.map(s => s.count);
    new Chart(elementId, {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          data,
          backgroundColor: '#2196F3'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }
}

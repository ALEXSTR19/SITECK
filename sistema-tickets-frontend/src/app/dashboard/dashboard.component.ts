import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { DashboardService } from '../services/dashboard.service';
import { TicketStat } from '../models/ticket-stat.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  dayChart?: Chart;
  weekChart?: Chart;
  monthChart?: Chart;

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getStatsByDay().subscribe(d => this.createChart('dayChart', d));
    this.dashboardService.getStatsByWeek().subscribe(d => this.createChart('weekChart', d));
    this.dashboardService.getStatsByMonth().subscribe(d => this.createChart('monthChart', d));
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

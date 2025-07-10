import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { TicketStat } from '../models/ticket-stat.model';
import { AdminStats } from '../models/admin-stats.model';
import { Rating } from '../models/rating.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  constructor(private http: HttpClient) {}

  getStatsByDay(): Observable<TicketStat[]> {
    return this.http.get<TicketStat[]>(`${environment.backendHost}/ticketStats/day`);
  }

  getStatsByWeek(): Observable<TicketStat[]> {
    return this.http.get<TicketStat[]>(`${environment.backendHost}/ticketStats/week`);
  }

  getStatsByMonth(): Observable<TicketStat[]> {
    return this.http.get<TicketStat[]>(`${environment.backendHost}/ticketStats/month`);
  }

  getTopServicios(): Observable<TicketStat[]> {
    return this.http.get<TicketStat[]>(`${environment.backendHost}/ticketStats/servicios`);
  }

  getTopClientes(): Observable<TicketStat[]> {
    return this.http.get<TicketStat[]>(`${environment.backendHost}/ticketStats/clientes`);
  }

  getTopTecnicos(): Observable<TicketStat[]> {
    return this.http.get<TicketStat[]>(`${environment.backendHost}/ticketStats/tecnicos`);
  }

  getAdminStats(): Observable<AdminStats> {
    return this.http.get<AdminStats>(`${environment.backendHost}/ticketStats/admin`);
  }

  getRatings(): Observable<Rating[]> {
    return this.http.get<Rating[]>(`${environment.backendHost}/ratings`);
  }
}

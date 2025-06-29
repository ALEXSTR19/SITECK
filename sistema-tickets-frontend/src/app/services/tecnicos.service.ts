import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tecnico, Ticket } from '../models/tecnicos.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class TecnicosService {

  constructor(private http: HttpClient) { }

  public getAllTickets(): Observable<Array<Ticket>> {
    return this.http.get<Array<Ticket>>(`${environment.backendHost}/tickets`);
  }

  public getAllTecnicos(): Observable<Array<Tecnico>> {
    return this.http.get<Array<Tecnico>>(`${environment.backendHost}/tecnicos`);
  }

  public getTicketsDeTecnico(codigo: string): Observable<Array<Ticket>>{
    return this.http.get<Array<Ticket>>(`${environment.backendHost}/tecnicos/${codigo}/tickets`);
  }

  public guardarTicket(formData: any): Observable<Ticket> {
    return this.http.post<Ticket>(`${environment.backendHost}/tickets`, formData);
  }
}
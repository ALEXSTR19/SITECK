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

  public getTecnicosPorServicio(nombreServicio: string): Observable<Array<Tecnico>> {
    return this.http.get<Array<Tecnico>>(
      `${environment.backendHost}/tecnicosPorEspecialidad`,
      { params: { especialidad: nombreServicio } }
    );
  }

  public getTicketsDeTecnico(codigo: string): Observable<Array<Ticket>>{
    return this.http.get<Array<Ticket>>(`${environment.backendHost}/tecnicos/${codigo}/tickets`);
  }

  public getTecnicoPorCodigo(codigo: string): Observable<Tecnico> {
    return this.http.get<Tecnico>(`${environment.backendHost}/tecnicos/${codigo}`);
  }

  public crearTecnico(tecnico: Tecnico): Observable<Tecnico> {
    return this.http.post<Tecnico>(`${environment.backendHost}/tecnicos`, tecnico);
  }

  public obtenerProximoCodigo(): Observable<string> {
    return this.http.get(`${environment.backendHost}/tecnicos/proximoCodigo`, { responseType: 'text' });
  }

  public guardarTicket(formData: any): Observable<Ticket> {
    return this.http.post<Ticket>(`${environment.backendHost}/tickets`, formData);
  }

  public getTicket(id: number): Observable<Ticket> {
    return this.http.get<Ticket>(`${environment.backendHost}/tickets/${id}`);
  }

  public editarTicket(id: number, formData: any): Observable<Ticket> {
    return this.http.put<Ticket>(`${environment.backendHost}/tickets/${id}`, formData);
  }

  public actualizarEstadoTicket(id: number, status: string): Observable<Ticket> {
    return this.http.put<Ticket>(
      `${environment.backendHost}/tickets/${id}/actualizarTicket`,
      null,
      {
        params: { status }
      }
    );
  }

  public getTicketHistory(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${environment.backendHost}/tickets/${id}/history`);
  }

  public deleteTicket(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.backendHost}/tickets/${id}`);
  }

  public restoreTicket(id: number): Observable<Ticket> {
    return this.http.put<Ticket>(`${environment.backendHost}/tickets/${id}/restore`, null);
  }
}
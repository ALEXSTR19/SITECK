import { Tecnico, Ticket } from './tecnicos.model';

export interface Rating {
  id: number;
  stars: number;
  comment: string;
  fecha: string;
  tecnico: Tecnico;
  ticket: Ticket;
}

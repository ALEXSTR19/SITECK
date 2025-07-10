import { Servicio } from './servicio.model';

export interface Tecnico {
    id: string;
    codigo: string;
    nombre: string;
    apellido: string;
    email?: string;
    username?: string;
    password?: string;
    especialidades: Servicio[];
    foto: string;
}

export interface Ticket {
    id: number;
    fecha: Date;
    cantidad: number;
    type: string;
    status: string;
    priority?: string;
    file: string;
    tecnico: Tecnico;
    servicio?: Servicio;
    instalacionEquipo?: string;
    instalacionModelo?: string;
    instalacionDireccion?: string;
    mantenimientoEquipo?: string;
    mantenimientoDescripcion?: string;
    mantenimientoProxima?: string;
    cotizacionCliente?: string;
    cotizacionMonto?: string;
    cotizacionDescripcion?: string;
    diagnosticoEquipo?: string;
    diagnosticoProblema?: string;
    diagnosticoObservaciones?: string;
    levantamientoCamaras?: string;
    levantamientoVideoportero?: string;
    levantamientoAlarma?: string;
    levantamientoControlAcceso?: string;
    levantamientoControlAsistencia?: string;
    levantamientoRedWifi?: string;
    levantamientoCercoElectrico?: string;
    reporteServicio?: string;
    reporteFotos?: string;
    pagado?: boolean;
}

export enum TicketType {
    INSTALACION = 0, MANTENIMIENTO = 1, VENTA = 2, COTIZACION = 3, DIAGNOSTICO = 4, LEVANTAMIENTO = 5
}

export enum TicketStatus {
    PENDIENTE = 0, EN_PROCESO = 1, FINALIZADO = 2, CANCELADO = 3
}

package com.compulandia.sistematickets.entities;

import java.time.LocalDate;

import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;
import com.compulandia.sistematickets.enums.TicketPriority;
import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.entities.Cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private double cantidad;

    private TypeTicket type;

    private TicketStatus status;

    private TicketPriority priority;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private boolean pagado = false;

    private String file;

    // Reporte realizado por el técnico al finalizar el servicio
    @Column(columnDefinition = "TEXT")
    private String reporteServicio;

    // Campos adicionales para cada tipo de servicio
    private String instalacionEquipo;
    private String instalacionModelo;
    private String instalacionDireccion;

    private String mantenimientoEquipo;
    private String mantenimientoDescripcion;
    private String mantenimientoProxima;

    private String cotizacionCliente;
    private String cotizacionMonto;
    private String cotizacionDescripcion;

    private String diagnosticoEquipo;
    private String diagnosticoProblema;
    private String diagnosticoObservaciones;

    // Campos para Levantamiento
    private String levantamientoCamaras;
    private String levantamientoVideoportero;
    private String levantamientoAlarma;
    private String levantamientoControlAcceso;
    private String levantamientoControlAsistencia;
    private String levantamientoRedWifi;
    private String levantamientoCercoElectrico;
    
    @ManyToOne
    private Tecnico tecnico;

    @ManyToOne
    private Servicio servicio;

    @ManyToOne
    private Cliente cliente;
}

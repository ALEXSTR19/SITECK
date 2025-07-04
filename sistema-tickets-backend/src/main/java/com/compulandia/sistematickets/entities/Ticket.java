package com.compulandia.sistematickets.entities;

import java.time.LocalDate;

import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;
import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.entities.Cliente;

import jakarta.persistence.Entity;
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

    private String file;

    // Reporte realizado por el técnico al finalizar el servicio
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
    
    @ManyToOne
    private Tecnico tecnico;

    @ManyToOne
    private Servicio servicio;

    @ManyToOne
    private Cliente cliente;
}

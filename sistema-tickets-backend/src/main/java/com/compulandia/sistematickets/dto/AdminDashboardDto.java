package com.compulandia.sistematickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDto {
    private Long serviciosPagados;
    private Long serviciosNoPagados;
    private Long serviciosFinalizadosPendiente;
    private Long serviciosFinalizadosPagados;
    private Long ticketsDelDia;
    private Long ticketsPendientes;
    private Long ticketsEnProceso;
    private Long ticketsFinalizados;
}

package com.compulandia.sistematickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatsDto {
    private String label;
    private Long count;
}

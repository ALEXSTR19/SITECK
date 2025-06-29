package com.compulandia.sistematickets.dto;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;

import com.compulandia.sistematickets.enums.TypeTicket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTicketDto {
    private double cantidad;

    private TypeTicket typeTicket;

    private LocalDate date;

    private String codigoUsuario;
}

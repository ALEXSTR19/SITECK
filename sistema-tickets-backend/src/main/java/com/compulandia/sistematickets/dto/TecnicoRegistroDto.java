package com.compulandia.sistematickets.dto;

import java.util.List;

import com.compulandia.sistematickets.entities.Servicio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TecnicoRegistroDto {
    private String nombre;
    private String apellido;
    private String codigo;
    private List<Servicio> especialidades;
    private String username;
    private String password;
}

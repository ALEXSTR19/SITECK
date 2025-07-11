package com.compulandia.sistematickets.dto;

import java.util.List;

import com.compulandia.sistematickets.entities.Servicio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TecnicoRegistroDto {
    private String nombre;
    private String apellido;
    private String codigo;
    private String email;
    private List<Servicio> especialidades;
    private String username;
    private String password;
}

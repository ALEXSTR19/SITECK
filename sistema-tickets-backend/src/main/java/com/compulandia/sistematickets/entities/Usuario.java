package com.compulandia.sistematickets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    private String username;
    private String password;
    private String servicios;
    private String role;

    // Codigo del tecnico asociado (opcional)
    private String codigoTecnico;
}

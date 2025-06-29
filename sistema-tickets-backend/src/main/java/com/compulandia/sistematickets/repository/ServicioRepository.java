package com.compulandia.sistematickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compulandia.sistematickets.entities.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    Servicio findByNombre(String nombre);
}

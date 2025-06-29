package com.compulandia.sistematickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compulandia.sistematickets.entities.Tecnico;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, String> {
    Tecnico findByCodigo(String codigo);

    List<Tecnico> findByEspecialidadesNombre(String nombre);

}

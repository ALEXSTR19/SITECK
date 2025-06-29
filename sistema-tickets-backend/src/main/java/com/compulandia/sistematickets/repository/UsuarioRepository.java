package com.compulandia.sistematickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compulandia.sistematickets.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Usuario findByUsername(String username);
}

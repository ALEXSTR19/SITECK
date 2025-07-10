package com.compulandia.sistematickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.compulandia.sistematickets.entities.Rating;
import com.compulandia.sistematickets.entities.Tecnico;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByTecnico(Tecnico tecnico);
}

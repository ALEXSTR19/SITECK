package com.compulandia.sistematickets.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stars;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime fecha;

    @ManyToOne
    private Tecnico tecnico;

    @ManyToOne
    private Ticket ticket;
}

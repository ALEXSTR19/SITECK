package com.compulandia.sistematickets.entities;

import java.time.LocalDateTime;

import com.compulandia.sistematickets.enums.TicketStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ticket ticket;

    private String action;

    private String changes;

    @Enumerated(EnumType.STRING)
    private TicketStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private TicketStatus newStatus;

    private LocalDateTime timestamp;
}

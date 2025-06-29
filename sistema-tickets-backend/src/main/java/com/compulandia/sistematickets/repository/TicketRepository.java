package com.compulandia.sistematickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByTecnicoCodigo(String codigo);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByType(TypeTicket typeTicket);

}

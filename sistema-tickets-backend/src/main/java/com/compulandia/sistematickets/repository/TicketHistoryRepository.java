package com.compulandia.sistematickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.compulandia.sistematickets.entities.TicketHistory;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
    List<TicketHistory> findByTicketIdOrderByTimestampAsc(Long ticketId);
}

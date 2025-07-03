package com.compulandia.sistematickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByTecnicoCodigo(String codigo);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByType(TypeTicket typeTicket);

    @Query(value = "SELECT fecha as label, COUNT(*) as count FROM ticket GROUP BY fecha ORDER BY fecha", nativeQuery = true)
    List<Object[]> countTicketsByDay();

    @Query(value = "SELECT CONCAT(YEAR(fecha), '-W', WEEK(fecha, 1)) as label, COUNT(*) as count FROM ticket GROUP BY YEAR(fecha), WEEK(fecha, 1) ORDER BY YEAR(fecha), WEEK(fecha, 1)", nativeQuery = true)
    List<Object[]> countTicketsByWeek();

    @Query(value = "SELECT DATE_FORMAT(fecha, '%Y-%m') as label, COUNT(*) as count FROM ticket GROUP BY DATE_FORMAT(fecha, '%Y-%m') ORDER BY DATE_FORMAT(fecha, '%Y-%m')", nativeQuery = true)
    List<Object[]> countTicketsByMonth();

}

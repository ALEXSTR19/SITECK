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
    List<Ticket> findByTecnicoCodigoAndDeletedFalse(String codigo);

    List<Ticket> findByStatusAndDeletedFalse(TicketStatus status);

    List<Ticket> findByTypeAndDeletedFalse(TypeTicket typeTicket);

    List<Ticket> findByDeletedFalse();

    List<Ticket> findByServicioNombreInAndDeletedFalse(List<String> nombres);

    @Query(value = "SELECT fecha as label, COUNT(*) as count FROM ticket GROUP BY fecha ORDER BY fecha", nativeQuery = true)
    List<Object[]> countTicketsByDay();

    @Query(value = "SELECT DATE_FORMAT(fecha, '%x-W%v') as label, COUNT(*) as count " +
            "FROM ticket GROUP BY DATE_FORMAT(fecha, '%x-W%v') ORDER BY DATE_FORMAT(fecha, '%x-W%v')",
            nativeQuery = true)
    List<Object[]> countTicketsByWeek();

    @Query(value = "SELECT DATE_FORMAT(fecha, '%Y-%m') as label, COUNT(*) as count FROM ticket GROUP BY DATE_FORMAT(fecha, '%Y-%m') ORDER BY DATE_FORMAT(fecha, '%Y-%m')", nativeQuery = true)
    List<Object[]> countTicketsByMonth();

    @Query(value = "SELECT s.nombre as label, COUNT(*) as count FROM ticket t JOIN servicio s ON t.servicio_id = s.id GROUP BY s.nombre ORDER BY count DESC LIMIT 5", nativeQuery = true)
    List<Object[]> topServicios();

    @Query(value = "SELECT CONCAT(c.nombre, ' ', c.apellido) as label, COUNT(*) as count FROM ticket t JOIN cliente c ON t.cliente_id = c.id GROUP BY c.id, c.nombre, c.apellido ORDER BY count DESC LIMIT 5", nativeQuery = true)
    List<Object[]> topClientes();

    @Query(value = "SELECT te.codigo as label, COUNT(*) as count FROM ticket t JOIN tecnico te ON t.tecnico_id = te.id GROUP BY te.codigo ORDER BY count DESC LIMIT 5", nativeQuery = true)
    List<Object[]> topTecnicos();

}

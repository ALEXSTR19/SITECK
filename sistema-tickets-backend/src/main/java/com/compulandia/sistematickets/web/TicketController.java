package com.compulandia.sistematickets.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.entities.TicketHistory;
import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.TicketRepository;
import com.compulandia.sistematickets.services.TicketService;
import com.compulandia.sistematickets.dto.TicketStatsDto;

import jakarta.annotation.PostConstruct;

@RestController
@CrossOrigin("*")
public class TicketController {
    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/tecnicos")
    public List<Tecnico> listarTecnicos(){
        return tecnicoRepository.findAll();
    }

    @GetMapping("/tecnicos/{codigo}")
    public Tecnico listarTecnicosPorCodigo(@PathVariable String codigo){
        return tecnicoRepository.findByCodigo(codigo);
    }
    @GetMapping("/tecnicosPorEspecialidad")
    public List<Tecnico> listarTecnicosPorEspecialidad(@RequestParam String especialidad){
        return tecnicoRepository.findByEspecialidadesNombre(especialidad);
    }

    @GetMapping("/tickets")
    public List<Ticket> listarTickets(){
        return ticketRepository.findByDeletedFalse();
    }
    @GetMapping("/tickets/{id}")
    public Ticket listarTicketPorId(@PathVariable Long id){
        return ticketRepository.findById(id).get();
    }
    @GetMapping("/tecnicos/{codigo}/tickets")
    public List<Ticket> listarTicketsPorCodigoTecnico(@PathVariable String codigo){
        Tecnico tecnico = tecnicoRepository.findByCodigo(codigo);
        if(tecnico == null){
            return List.of();
        }
        List<String> servicios = tecnico.getEspecialidades().stream()
                .map(Servicio::getNombre)
                .toList();
        return ticketRepository.findByServicioNombreInAndDeletedFalse(servicios);
    }
    @GetMapping("/ticketsPorStatus")
    public List<Ticket> listarTicketsPorStatus(@RequestParam TicketStatus status){
        return ticketRepository.findByStatusAndDeletedFalse(status);
    }
    @GetMapping("/tickets/porTipo")
     public List<Ticket> listarTicketsPorType(@RequestParam TypeTicket type){
        return ticketRepository.findByTypeAndDeletedFalse(type);
    }

    @PutMapping("/tickets/{ticketId}/actualizarTicket")
    public Ticket actualizarStatusDeTicket(@RequestParam TicketStatus status,
                                           @RequestParam String codigo,
                                           @PathVariable Long ticketId){
        return ticketService.actualizaTicketPorStatus(status, ticketId, codigo);
    }

    @PutMapping(path = "/tickets/{ticketId}/finalizar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ticket finalizarTicket(@PathVariable Long ticketId,
                                  @RequestParam String reporte,
                                  @RequestParam(value="fotos", required = false) MultipartFile[] fotos) throws IOException{
        return ticketService.finalizarConReporte(ticketId, reporte, fotos);
    }

    @GetMapping("/ticketStats/day")
    public List<TicketStatsDto> ticketStatsByDay(){
        return ticketService.getStatsByDay();
    }

    @GetMapping("/ticketStats/week")
    public List<TicketStatsDto> ticketStatsByWeek(){
        return ticketService.getStatsByWeek();
    }

    @GetMapping("/ticketStats/month")
    public List<TicketStatsDto> ticketStatsByMonth(){
        return ticketService.getStatsByMonth();
    }

    @GetMapping("/ticketStats/servicios")
    public List<TicketStatsDto> topServicios(){
        return ticketService.getTopServicios();
    }

    @GetMapping("/ticketStats/clientes")
    public List<TicketStatsDto> topClientes(){
        return ticketService.getTopClientes();
    }

    @GetMapping("/ticketStats/tecnicos")
    public List<TicketStatsDto> topTecnicos(){
        return ticketService.getTopTecnicos();
    }
@PostMapping(path = "/tickets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ticket guardarTicket(
        @RequestParam(value="file", required=false) MultipartFile file,
        @RequestParam("tecnicoCodigo") String tecnicoCodigo,
        @RequestParam("cantidad") double cantidad,
        @RequestParam("servicio") String servicio,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
    @RequestParam(value="priority", required = false) String priority,
    @RequestParam(value="clienteId", required = false) Long clienteId,
    @RequestParam(value="instalacionEquipo", required = false) String instalacionEquipo,
    @RequestParam(value="instalacionModelo", required = false) String instalacionModelo,
    @RequestParam(value="instalacionDireccion", required = false) String instalacionDireccion,
    @RequestParam(value="mantenimientoEquipo", required = false) String mantenimientoEquipo,
    @RequestParam(value="mantenimientoDescripcion", required = false) String mantenimientoDescripcion,
    @RequestParam(value="mantenimientoProxima", required = false) String mantenimientoProxima,
    @RequestParam(value="cotizacionCliente", required = false) String cotizacionCliente,
    @RequestParam(value="cotizacionMonto", required = false) String cotizacionMonto,
    @RequestParam(value="cotizacionDescripcion", required = false) String cotizacionDescripcion,
    @RequestParam(value="diagnosticoEquipo", required = false) String diagnosticoEquipo,
    @RequestParam(value="diagnosticoProblema", required = false) String diagnosticoProblema,
    @RequestParam(value="diagnosticoObservaciones", required = false) String diagnosticoObservaciones,
    @RequestParam(value="pagado", defaultValue = "false") boolean pagado
) throws IOException {
    return ticketService.saveTicket(
        file,
        tecnicoCodigo,
        cantidad,
        servicio,
        date,
        priority,
        clienteId,
        instalacionEquipo,
        instalacionModelo,
        instalacionDireccion,
        mantenimientoEquipo,
        mantenimientoDescripcion,
        mantenimientoProxima,
        cotizacionCliente,
        cotizacionMonto,
        cotizacionDescripcion,
        diagnosticoEquipo,
        diagnosticoProblema,
        diagnosticoObservaciones,
        pagado
    );
}

    @GetMapping(value = "/ticketfile/{ticketId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] listarArchivoPorId(@PathVariable Long ticketId) throws IOException{
        return ticketService.getArchivoPorId(ticketId);
    }

    @PutMapping(path = "/tickets/{ticketId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ticket actualizarTicket(
        @PathVariable Long ticketId,
        @RequestParam(value="file", required=false) MultipartFile file,
        @RequestParam("tecnicoCodigo") String tecnicoCodigo,
        @RequestParam("cantidad") double cantidad,
        @RequestParam("servicio") String servicio,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(value="priority", required = false) String priority,
        @RequestParam(value="clienteId", required = false) Long clienteId,
        @RequestParam(value="instalacionEquipo", required = false) String instalacionEquipo,
        @RequestParam(value="instalacionModelo", required = false) String instalacionModelo,
        @RequestParam(value="instalacionDireccion", required = false) String instalacionDireccion,
        @RequestParam(value="mantenimientoEquipo", required = false) String mantenimientoEquipo,
        @RequestParam(value="mantenimientoDescripcion", required = false) String mantenimientoDescripcion,
        @RequestParam(value="mantenimientoProxima", required = false) String mantenimientoProxima,
        @RequestParam(value="cotizacionCliente", required = false) String cotizacionCliente,
        @RequestParam(value="cotizacionMonto", required = false) String cotizacionMonto,
        @RequestParam(value="cotizacionDescripcion", required = false) String cotizacionDescripcion,
        @RequestParam(value="diagnosticoEquipo", required = false) String diagnosticoEquipo,
        @RequestParam(value="diagnosticoProblema", required = false) String diagnosticoProblema,
        @RequestParam(value="diagnosticoObservaciones", required = false) String diagnosticoObservaciones,
        @RequestParam(value="pagado", defaultValue = "false") boolean pagado
    ) throws IOException {
        return ticketService.updateTicket(
            ticketId,
            file,
            tecnicoCodigo,
            cantidad,
            servicio,
            date,
            priority,
            clienteId,
            instalacionEquipo,
            instalacionModelo,
            instalacionDireccion,
            mantenimientoEquipo,
            mantenimientoDescripcion,
            mantenimientoProxima,
            cotizacionCliente,
            cotizacionMonto,
            cotizacionDescripcion,
            diagnosticoEquipo,
            diagnosticoProblema,
            diagnosticoObservaciones,
            pagado
        );
    }

    @DeleteMapping("/tickets/{ticketId}")
    public void eliminarTicket(@PathVariable Long ticketId){
        ticketService.deleteTicket(ticketId);
    }

    @PutMapping("/tickets/{ticketId}/restore")
    public Ticket restaurarTicket(@PathVariable Long ticketId){
        return ticketService.restoreTicket(ticketId);
    }

    @GetMapping("/tickets/{ticketId}/history")
    public List<TicketHistory> historialTicket(@PathVariable Long ticketId){
        return ticketService.getHistory(ticketId);
    }
}



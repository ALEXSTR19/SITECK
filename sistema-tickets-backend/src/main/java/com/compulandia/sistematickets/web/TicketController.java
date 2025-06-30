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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.TicketRepository;
import com.compulandia.sistematickets.services.TicketService;

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
        return ticketRepository.findAll();
    }
    @GetMapping("/tickets/{id}")
    public Ticket listarTicketPorId(@RequestParam Long Id){
        return ticketRepository.findById(Id).get();
    }
    @GetMapping("/tecnicos/{codigo}/tickets")
    public List<Ticket> listarTicketsPorCodigoTecnico(@PathVariable String codigo){
        return ticketRepository.findByTecnicoCodigo(codigo);
    }
    @GetMapping("/ticketsPorStatus")
    public List<Ticket> listarTicketsPorStatus(@RequestParam TicketStatus status){
        return ticketRepository.findByStatus(status);
    }
    @GetMapping("/tickets/porTipo")
     public List<Ticket> listarTicketsPorType(@RequestParam TypeTicket type){
        return ticketRepository.findByType(type);
    }  

    @PutMapping("/tickets/{ticketId}/actualizarTicket")
    public Ticket actualizarStatusDeTicket(@RequestParam TicketStatus status, @PathVariable Long ticketId){
        return ticketService.actualizaTicketPorStatus(status, ticketId);
    }
@PostMapping(path = "/tickets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public Ticket guardarTicket(
    @RequestParam(value="file", required=false) MultipartFile file,
    @RequestParam("cantidad") double cantidad,
    @RequestParam("servicio") String servicio,
    @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
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
    @RequestParam(value="mecpNombre", required = false) String mecpNombre,
    @RequestParam(value="mecpTelefono", required = false) String mecpTelefono,
    @RequestParam(value="mecpDireccion", required = false) String mecpDireccion,
    @RequestParam(value="mecpModelo", required = false) String mecpModelo,
    @RequestParam(value="mecpNombreEquipo", required = false) String mecpNombreEquipo,
    @RequestParam(value="mecpAccesorios", required = false) String mecpAccesorios,
    @RequestParam(value="mecpDiagnostico", required = false) String mecpDiagnostico,
    @RequestParam(value="mecpDetalles", required = false) String mecpDetalles
) throws IOException {
    return ticketService.saveTicket(
        file,
        cantidad,
        servicio,
        date,
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
        mecpNombre,
        mecpTelefono,
        mecpDireccion,
        mecpModelo,
        mecpNombreEquipo,
        mecpAccesorios,
        mecpDiagnostico,
        mecpDetalles
    );
}

    @GetMapping(value = "/ticketfile/{ticketId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] listarArchivoPorId(@PathVariable Long ticketId) throws IOException{
        return ticketService.getArchivoPorId(ticketId);
    }
   
    }



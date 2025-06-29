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
        return tecnicoRepository.findByEspecialidad(especialidad);
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
    @RequestParam("type") TypeTicket type,
    @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
    @RequestParam("codigoTecnico") String codigoTecnico
) throws IOException {
    return ticketService.saveTicket(file, cantidad, type, date, codigoTecnico);
}

    @GetMapping(value = "/ticketfile/{ticketId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] listarArchivoPorId(@PathVariable Long ticketId) throws IOException{
        return ticketService.getArchivoPorId(ticketId);
    }
   
    }



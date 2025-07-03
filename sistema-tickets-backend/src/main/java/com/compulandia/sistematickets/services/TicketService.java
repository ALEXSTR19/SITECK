package com.compulandia.sistematickets.services;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.dto.TicketStatsDto;
import com.compulandia.sistematickets.entities.Cliente;
import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.TicketRepository;
import com.compulandia.sistematickets.repository.ClienteRepository;
import com.compulandia.sistematickets.repository.ServicioRepository;


import jakarta.transaction.Transactional;

@Service
@Transactional
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public Ticket saveTicket(MultipartFile file, String tecnicoCodigo, double cantidad, String servicioNombre, LocalDate date,
            Long clienteId,
            String instalacionEquipo,
            String instalacionModelo,
            String instalacionDireccion,
            String mantenimientoEquipo,
            String mantenimientoDescripcion,
            String mantenimientoProxima,
            String cotizacionCliente,
            String cotizacionMonto,
            String cotizacionDescripcion,
            String diagnosticoEquipo,
            String diagnosticoProblema,
            String diagnosticoObservaciones) throws IOException {

        Path filePath = null;
        if (file != null && !file.isEmpty()) {
            /*
             * se creo una ruta donde se guarda el archivo
             * system.getProperty(user.home): obtiene la ruta del directorio del usuario del
             * S.O
             * Paths.get(...): crea un objeto Path apuntando a una carpeta llamada
             * enset/tickets dentro del codigo
             */
            Path FolderPath = Paths.get(System.getProperty("user.home"), "enset-data", "tickets");

            if (!Files.exists(FolderPath)) {
                Files.createDirectories(FolderPath);
            }

            String fileName = UUID.randomUUID().toString();

            // sE CREA UN PATH PARA EL ARCHIVO PDF QUE SE GUARDARA EN ENSET-DATA
            filePath = Paths.get(System.getProperty("user.home"), "enset-data", "tickets", fileName + ".pdf");

            // file.getInputStream(): obtiene el flujo de datos del archivo recibido desde
            // la solicitud HTTP
            // Files.copy(...): Copia los datos del archivo al destino filePath
            Files.copy(file.getInputStream(), filePath);
        }

        Servicio servicio = servicioRepository.findByNombre(servicioNombre);
        if (servicio == null) {
            servicio = servicioRepository.save(Servicio.builder().nombre(servicioNombre).build());
        }

        TypeTicket typeTicket = null;
        try {
            typeTicket = TypeTicket.valueOf(servicioNombre.toUpperCase());
        } catch (IllegalArgumentException e) {
            // El nombre del servicio no corresponde con ningún TypeTicket definido
        }

        Tecnico tecnico = null;
        if (tecnicoCodigo != null && !tecnicoCodigo.isEmpty()) {
            tecnico = tecnicoRepository.findByCodigo(tecnicoCodigo);
        }
        if (tecnico == null) {
            var tecnicos = tecnicoRepository.findByEspecialidadesNombre(servicioNombre);
            if (!tecnicos.isEmpty()) {
                tecnico = tecnicos.get(0);
            }
        }

        Cliente cliente = null;
        if (clienteId != null) {
            cliente = clienteRepository.findById(clienteId).orElse(null);
        }

        Ticket ticket = Ticket.builder()
                .status(TicketStatus.PENDIENTE)
                .fecha(date)
                .tecnico(tecnico)
                .servicio(servicio)
                .cliente(cliente)
                .type(typeTicket)
                .cantidad(cantidad)
                .file(filePath != null ? filePath.toUri().toString() : null)
                .instalacionEquipo(instalacionEquipo)
                .instalacionModelo(instalacionModelo)
                .instalacionDireccion(instalacionDireccion)
                .mantenimientoEquipo(mantenimientoEquipo)
                .mantenimientoDescripcion(mantenimientoDescripcion)
                .mantenimientoProxima(mantenimientoProxima)
                .cotizacionCliente(cotizacionCliente)
                .cotizacionMonto(cotizacionMonto)
                .cotizacionDescripcion(cotizacionDescripcion)
                .diagnosticoEquipo(diagnosticoEquipo)
                .diagnosticoProblema(diagnosticoProblema)
                .diagnosticoObservaciones(diagnosticoObservaciones)
                .build();

        return ticketRepository.save(ticket);

    }

    public Ticket updateTicket(Long id,
            MultipartFile file,
            String tecnicoCodigo,
            double cantidad,
            String servicioNombre,
            LocalDate date,
            Long clienteId,
            String instalacionEquipo,
            String instalacionModelo,
            String instalacionDireccion,
            String mantenimientoEquipo,
            String mantenimientoDescripcion,
            String mantenimientoProxima,
            String cotizacionCliente,
            String cotizacionMonto,
            String cotizacionDescripcion,
            String diagnosticoEquipo,
            String diagnosticoProblema,
            String diagnosticoObservaciones) throws IOException {

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        Path filePath = null;
        if (file != null && !file.isEmpty()) {
            Path FolderPath = Paths.get(System.getProperty("user.home"), "enset-data", "tickets");
            if (!Files.exists(FolderPath)) {
                Files.createDirectories(FolderPath);
            }
            String fileName = UUID.randomUUID().toString();
            filePath = Paths.get(System.getProperty("user.home"), "enset-data", "tickets", fileName + ".pdf");
            Files.copy(file.getInputStream(), filePath);
            ticket.setFile(filePath.toUri().toString());
        }

        Servicio servicio = servicioRepository.findByNombre(servicioNombre);
        if (servicio == null) {
            servicio = servicioRepository.save(Servicio.builder().nombre(servicioNombre).build());
        }

        TypeTicket typeTicket = null;
        try {
            typeTicket = TypeTicket.valueOf(servicioNombre.toUpperCase());
        } catch (IllegalArgumentException e) {
        }

        Tecnico tecnico = null;
        if (tecnicoCodigo != null && !tecnicoCodigo.isEmpty()) {
            tecnico = tecnicoRepository.findByCodigo(tecnicoCodigo);
        }
        if (tecnico == null) {
            var tecnicos = tecnicoRepository.findByEspecialidadesNombre(servicioNombre);
            if (!tecnicos.isEmpty()) {
                tecnico = tecnicos.get(0);
            }
        }

        Cliente cliente = null;
        if (clienteId != null) {
            cliente = clienteRepository.findById(clienteId).orElse(null);
        }

        ticket.setFecha(date);
        ticket.setCantidad(cantidad);
        ticket.setServicio(servicio);
        ticket.setType(typeTicket);
        ticket.setTecnico(tecnico);
        ticket.setCliente(cliente);
        ticket.setInstalacionEquipo(instalacionEquipo);
        ticket.setInstalacionModelo(instalacionModelo);
        ticket.setInstalacionDireccion(instalacionDireccion);
        ticket.setMantenimientoEquipo(mantenimientoEquipo);
        ticket.setMantenimientoDescripcion(mantenimientoDescripcion);
        ticket.setMantenimientoProxima(mantenimientoProxima);
        ticket.setCotizacionCliente(cotizacionCliente);
        ticket.setCotizacionMonto(cotizacionMonto);
        ticket.setCotizacionDescripcion(cotizacionDescripcion);
        ticket.setDiagnosticoEquipo(diagnosticoEquipo);
        ticket.setDiagnosticoProblema(diagnosticoProblema);
        ticket.setDiagnosticoObservaciones(diagnosticoObservaciones);

        return ticketRepository.save(ticket);
    }
    public byte[] getArchivoPorId(Long ticketId) throws IOException{
        Ticket ticket = ticketRepository.findById(ticketId).get();

        /*- ticket.getFile():obtiene la URI del archivo guardado
         * -URI.create(...): convierte la cadena en objeto URI
         * -Path.of(...): Convierte el URI en un Path
         * -Files.readAllBytes(...): lee el contenido del archivo y lo devuelve como un array de bytes
         */
        if (ticket.getFile() == null) {
            return null;
        }
        return Files.readAllBytes(Path.of(URI.create(ticket.getFile())));


    }
    public Ticket actualizaTicketPorStatus(TicketStatus status, long id){
        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    public List<TicketStatsDto> getStatsByDay() {
        return ticketRepository.countTicketsByDay().stream()
                .map(arr -> new TicketStatsDto(arr[0].toString(), ((Number) arr[1]).longValue()))
                .toList();
    }

    public List<TicketStatsDto> getStatsByWeek() {
        return ticketRepository.countTicketsByWeek().stream()
                .map(arr -> new TicketStatsDto(arr[0].toString(), ((Number) arr[1]).longValue()))
                .toList();
    }

    public List<TicketStatsDto> getStatsByMonth() {
        return ticketRepository.countTicketsByMonth().stream()
                .map(arr -> new TicketStatsDto(arr[0].toString(), ((Number) arr[1]).longValue()))
                .toList();
    }

    public List<TicketStatsDto> getTopServicios() {
        return ticketRepository.topServicios().stream()
                .map(arr -> new TicketStatsDto(arr[0].toString(), ((Number) arr[1]).longValue()))
                .toList();
    }

    public List<TicketStatsDto> getTopClientes() {
        return ticketRepository.topClientes().stream()
                .map(arr -> new TicketStatsDto(arr[0].toString(), ((Number) arr[1]).longValue()))
                .toList();
    }

    public List<TicketStatsDto> getTopTecnicos() {
        return ticketRepository.topTecnicos().stream()
                .map(arr -> new TicketStatsDto(arr[0].toString(), ((Number) arr[1]).longValue()))
                .toList();
    }
}

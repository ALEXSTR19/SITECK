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
import com.compulandia.sistematickets.entities.TicketHistory;
import com.compulandia.sistematickets.dto.TicketStatsDto;
import com.compulandia.sistematickets.dto.AdminDashboardDto;
import com.compulandia.sistematickets.entities.Cliente;
import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;
import com.compulandia.sistematickets.enums.TicketPriority;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.TicketRepository;
import com.compulandia.sistematickets.repository.TicketHistoryRepository;
import com.compulandia.sistematickets.repository.ClienteRepository;
import com.compulandia.sistematickets.repository.ServicioRepository;
import com.compulandia.sistematickets.services.NotificationService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


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

    @Autowired
    private TicketHistoryRepository historyRepository;

    @Autowired
    private NotificationService notificationService;
    private void saveHistory(Ticket ticket, String action, TicketStatus previous, TicketStatus current, String changes) {
        historyRepository.save(TicketHistory.builder()
                .ticket(ticket)
                .action(action)
                .changes(changes)
                .previousStatus(previous)
                .newStatus(current)
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }

    public Ticket saveTicket(MultipartFile file, String tecnicoCodigo, double cantidad, String servicioNombre, LocalDate date,
            String horaVisita,
            Double costoTotal,
            Double anticipo,
            String priority,
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
            String diagnosticoObservaciones,
            String levantamientoCamaras,
            String levantamientoVideoportero,
            String levantamientoAlarma,
            String levantamientoControlAcceso,
            String levantamientoControlAsistencia,
            String levantamientoRedWifi,
            String levantamientoCercoElectrico,
            boolean pagado) throws IOException {

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
        if (servicioNombre != null) {
            String nombreUpper = servicioNombre.toUpperCase();
            if (nombreUpper.contains("INSTAL")) {
                typeTicket = TypeTicket.INSTALACION;
            } else if (nombreUpper.contains("MANTEN")) {
                typeTicket = TypeTicket.MANTENIMIENTO;
            } else if (nombreUpper.contains("COTIZ")) {
                typeTicket = TypeTicket.COTIZACION;
            } else if (nombreUpper.contains("DIAGN")) {
                typeTicket = TypeTicket.DIAGNOSTICO;
            } else if (nombreUpper.contains("LEVANT")) {
                typeTicket = TypeTicket.LEVANTAMIENTO;
            } else if (nombreUpper.contains("VENTA")) {
                typeTicket = TypeTicket.VENTA;
            }
        }

        Tecnico tecnico = null;
        if (tecnicoCodigo != null && !tecnicoCodigo.isEmpty()) {
            tecnico = tecnicoRepository.findByCodigo(tecnicoCodigo);
        }
        if (tecnico == null && servicio.getLiderCodigo() != null) {
            tecnico = tecnicoRepository.findByCodigo(servicio.getLiderCodigo());
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


        TicketPriority priorityEnum = null;
        if (priority != null) {
            try {
                priorityEnum = TicketPriority.valueOf(priority.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore invalid priority
            }
        }

        boolean pagadoFinal = pagado;
        if(!pagadoFinal && anticipo != null && costoTotal != null && anticipo >= costoTotal){
            pagadoFinal = true;
        }

        Ticket ticket = Ticket.builder()
                .status(TicketStatus.PENDIENTE)
                .fecha(date)
                .horaVisita(horaVisita)
                .tecnico(tecnico)
                .servicio(servicio)
                .cliente(cliente)
                .priority(priorityEnum)
                .type(typeTicket)
                .cantidad(cantidad)
                .costoTotal(costoTotal)
                .anticipo(anticipo)
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
                .levantamientoCamaras(levantamientoCamaras)
                .levantamientoVideoportero(levantamientoVideoportero)
                .levantamientoAlarma(levantamientoAlarma)
                .levantamientoControlAcceso(levantamientoControlAcceso)
                .levantamientoControlAsistencia(levantamientoControlAsistencia)
                .levantamientoRedWifi(levantamientoRedWifi)
                .levantamientoCercoElectrico(levantamientoCercoElectrico)
                .pagado(pagadoFinal)
                .build();

        Ticket saved = ticketRepository.save(ticket);
        if (tecnico != null) {
            notificationService.notifyTicketAssigned(tecnico, saved);
        }
        saveHistory(saved, "CREATED", null, saved.getStatus(), null);
        return saved;

    }

    public Ticket updateTicket(Long id,
            MultipartFile file,
            String tecnicoCodigo,
            double cantidad,
            String servicioNombre,
            LocalDate date,
            String horaVisita,
            Double costoTotal,
            Double anticipo,
            String priority,
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
            String diagnosticoObservaciones,
            String levantamientoCamaras,
            String levantamientoVideoportero,
            String levantamientoAlarma,
            String levantamientoControlAcceso,
            String levantamientoControlAsistencia,
            String levantamientoRedWifi,
            String levantamientoCercoElectrico,
            boolean pagado) throws IOException {

        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        Tecnico oldTecnico = ticket.getTecnico();
        TicketStatus previousStatus = ticket.getStatus();
        StringBuilder changes = new StringBuilder();

        TicketPriority priorityEnum = null;
        if (priority != null) {
            try {
                priorityEnum = TicketPriority.valueOf(priority.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore invalid priority
            }
        }

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
        if (servicioNombre != null) {
            String nombreUpper = servicioNombre.toUpperCase();
            if (nombreUpper.contains("INSTAL")) {
                typeTicket = TypeTicket.INSTALACION;
            } else if (nombreUpper.contains("MANTEN")) {
                typeTicket = TypeTicket.MANTENIMIENTO;
            } else if (nombreUpper.contains("COTIZ")) {
                typeTicket = TypeTicket.COTIZACION;
            } else if (nombreUpper.contains("DIAGN")) {
                typeTicket = TypeTicket.DIAGNOSTICO;
            } else if (nombreUpper.contains("LEVANT")) {
                typeTicket = TypeTicket.LEVANTAMIENTO;
            } else if (nombreUpper.contains("VENTA")) {
                typeTicket = TypeTicket.VENTA;
            }
        }

        Tecnico tecnico = null;
        if (tecnicoCodigo != null && !tecnicoCodigo.isEmpty()) {
            tecnico = tecnicoRepository.findByCodigo(tecnicoCodigo);
        }
        if (tecnico == null && servicio.getLiderCodigo() != null) {
            tecnico = tecnicoRepository.findByCodigo(servicio.getLiderCodigo());
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

        if (!ticket.getFecha().equals(date)) {
            changes.append("fecha: ").append(ticket.getFecha()).append(" -> ").append(date).append("; ");
            ticket.setFecha(date);
        }
        if (!equalsStr(ticket.getHoraVisita(), horaVisita)) {
            changes.append("horaVisita: ").append(ticket.getHoraVisita()).append(" -> ").append(horaVisita).append("; ");
            ticket.setHoraVisita(horaVisita);
        }
        if (ticket.getCantidad() != cantidad) {
            changes.append("cantidad: ").append(ticket.getCantidad()).append(" -> ").append(cantidad).append("; ");
            ticket.setCantidad(cantidad);
        }
        if (ticket.getServicio() == null || !ticket.getServicio().getNombre().equals(servicioNombre)) {
            String oldVal = ticket.getServicio() != null ? ticket.getServicio().getNombre() : "null";
            changes.append("servicio: ").append(oldVal).append(" -> ").append(servicioNombre).append("; ");
            ticket.setServicio(servicio);
        }
        if (ticket.getType() != typeTicket) {
            changes.append("type: ").append(ticket.getType()).append(" -> ").append(typeTicket).append("; ");
            ticket.setType(typeTicket);
        }
        if (ticket.getTecnico() == null || !ticket.getTecnico().equals(tecnico)) {
            String oldVal = ticket.getTecnico() != null ? ticket.getTecnico().getCodigo() : "null";
            String newVal = tecnico != null ? tecnico.getCodigo() : "null";
            changes.append("tecnico: ").append(oldVal).append(" -> ").append(newVal).append("; ");
            ticket.setTecnico(tecnico);
        }
        if (ticket.getCliente() == null || !ticket.getCliente().equals(cliente)) {
            String oldVal = ticket.getCliente() != null ? ticket.getCliente().getId() + "" : "null";
            String newVal = cliente != null ? cliente.getId() + "" : "null";
            changes.append("cliente: ").append(oldVal).append(" -> ").append(newVal).append("; ");
            ticket.setCliente(cliente);
        }
        if (ticket.getPriority() != priorityEnum) {
            String oldVal = ticket.getPriority() != null ? ticket.getPriority().name() : "null";
            String newVal = priorityEnum != null ? priorityEnum.name() : "null";
            changes.append("priority: ").append(oldVal).append(" -> ").append(newVal).append("; ");
            ticket.setPriority(priorityEnum);
        }
        if (!equalsStr(ticket.getInstalacionEquipo(), instalacionEquipo)) {
            changes.append("instalacionEquipo: ").append(ticket.getInstalacionEquipo()).append(" -> ").append(instalacionEquipo).append("; ");
            ticket.setInstalacionEquipo(instalacionEquipo);
        }
        if (!equalsStr(ticket.getInstalacionModelo(), instalacionModelo)) {
            changes.append("instalacionModelo: ").append(ticket.getInstalacionModelo()).append(" -> ").append(instalacionModelo).append("; ");
            ticket.setInstalacionModelo(instalacionModelo);
        }
        if (!equalsStr(ticket.getInstalacionDireccion(), instalacionDireccion)) {
            changes.append("instalacionDireccion: ").append(ticket.getInstalacionDireccion()).append(" -> ").append(instalacionDireccion).append("; ");
            ticket.setInstalacionDireccion(instalacionDireccion);
        }
        if (!equalsStr(ticket.getMantenimientoEquipo(), mantenimientoEquipo)) {
            changes.append("mantenimientoEquipo: ").append(ticket.getMantenimientoEquipo()).append(" -> ").append(mantenimientoEquipo).append("; ");
            ticket.setMantenimientoEquipo(mantenimientoEquipo);
        }
        if (!equalsStr(ticket.getMantenimientoDescripcion(), mantenimientoDescripcion)) {
            changes.append("mantenimientoDescripcion: ").append(ticket.getMantenimientoDescripcion()).append(" -> ").append(mantenimientoDescripcion).append("; ");
            ticket.setMantenimientoDescripcion(mantenimientoDescripcion);
        }
        if (!equalsStr(ticket.getMantenimientoProxima(), mantenimientoProxima)) {
            changes.append("mantenimientoProxima: ").append(ticket.getMantenimientoProxima()).append(" -> ").append(mantenimientoProxima).append("; ");
            ticket.setMantenimientoProxima(mantenimientoProxima);
        }
        if (!equalsStr(ticket.getCotizacionCliente(), cotizacionCliente)) {
            changes.append("cotizacionCliente: ").append(ticket.getCotizacionCliente()).append(" -> ").append(cotizacionCliente).append("; ");
            ticket.setCotizacionCliente(cotizacionCliente);
        }
        if (!equalsStr(ticket.getCotizacionMonto(), cotizacionMonto)) {
            changes.append("cotizacionMonto: ").append(ticket.getCotizacionMonto()).append(" -> ").append(cotizacionMonto).append("; ");
            ticket.setCotizacionMonto(cotizacionMonto);
        }
        if (!equalsStr(ticket.getCotizacionDescripcion(), cotizacionDescripcion)) {
            changes.append("cotizacionDescripcion: ").append(ticket.getCotizacionDescripcion()).append(" -> ").append(cotizacionDescripcion).append("; ");
            ticket.setCotizacionDescripcion(cotizacionDescripcion);
        }
        if (!equalsStr(ticket.getDiagnosticoEquipo(), diagnosticoEquipo)) {
            changes.append("diagnosticoEquipo: ").append(ticket.getDiagnosticoEquipo()).append(" -> ").append(diagnosticoEquipo).append("; ");
            ticket.setDiagnosticoEquipo(diagnosticoEquipo);
        }
        if (!equalsStr(ticket.getDiagnosticoProblema(), diagnosticoProblema)) {
            changes.append("diagnosticoProblema: ").append(ticket.getDiagnosticoProblema()).append(" -> ").append(diagnosticoProblema).append("; ");
            ticket.setDiagnosticoProblema(diagnosticoProblema);
        }
        if (!equalsStr(ticket.getDiagnosticoObservaciones(), diagnosticoObservaciones)) {
            changes.append("diagnosticoObservaciones: ").append(ticket.getDiagnosticoObservaciones()).append(" -> ").append(diagnosticoObservaciones).append("; ");
            ticket.setDiagnosticoObservaciones(diagnosticoObservaciones);
        }
        if (!equalsStr(ticket.getLevantamientoCamaras(), levantamientoCamaras)) {
            changes.append("levantamientoCamaras: ").append(ticket.getLevantamientoCamaras()).append(" -> ").append(levantamientoCamaras).append("; ");
            ticket.setLevantamientoCamaras(levantamientoCamaras);
        }
        if (!equalsStr(ticket.getLevantamientoVideoportero(), levantamientoVideoportero)) {
            changes.append("levantamientoVideoportero: ").append(ticket.getLevantamientoVideoportero()).append(" -> ").append(levantamientoVideoportero).append("; ");
            ticket.setLevantamientoVideoportero(levantamientoVideoportero);
        }
        if (!equalsStr(ticket.getLevantamientoAlarma(), levantamientoAlarma)) {
            changes.append("levantamientoAlarma: ").append(ticket.getLevantamientoAlarma()).append(" -> ").append(levantamientoAlarma).append("; ");
            ticket.setLevantamientoAlarma(levantamientoAlarma);
        }
        if (!equalsStr(ticket.getLevantamientoControlAcceso(), levantamientoControlAcceso)) {
            changes.append("levantamientoControlAcceso: ").append(ticket.getLevantamientoControlAcceso()).append(" -> ").append(levantamientoControlAcceso).append("; ");
            ticket.setLevantamientoControlAcceso(levantamientoControlAcceso);
        }
        if (!equalsStr(ticket.getLevantamientoControlAsistencia(), levantamientoControlAsistencia)) {
            changes.append("levantamientoControlAsistencia: ").append(ticket.getLevantamientoControlAsistencia()).append(" -> ").append(levantamientoControlAsistencia).append("; ");
            ticket.setLevantamientoControlAsistencia(levantamientoControlAsistencia);
        }
        if (!equalsStr(ticket.getLevantamientoRedWifi(), levantamientoRedWifi)) {
            changes.append("levantamientoRedWifi: ").append(ticket.getLevantamientoRedWifi()).append(" -> ").append(levantamientoRedWifi).append("; ");
            ticket.setLevantamientoRedWifi(levantamientoRedWifi);
        }
        if (!equalsStr(ticket.getLevantamientoCercoElectrico(), levantamientoCercoElectrico)) {
            changes.append("levantamientoCercoElectrico: ").append(ticket.getLevantamientoCercoElectrico()).append(" -> ").append(levantamientoCercoElectrico).append("; ");
            ticket.setLevantamientoCercoElectrico(levantamientoCercoElectrico);
        }
        if (ticket.getCostoTotal() == null || !ticket.getCostoTotal().equals(costoTotal)) {
            changes.append("costoTotal: ").append(ticket.getCostoTotal()).append(" -> ").append(costoTotal).append("; ");
            ticket.setCostoTotal(costoTotal);
        }
        if (ticket.getAnticipo() == null || !ticket.getAnticipo().equals(anticipo)) {
            changes.append("anticipo: ").append(ticket.getAnticipo()).append(" -> ").append(anticipo).append("; ");
            ticket.setAnticipo(anticipo);
        }

        boolean pagadoFinal = pagado;
        if(!pagadoFinal && anticipo != null && costoTotal != null && anticipo >= costoTotal){
            pagadoFinal = true;
        }
        if (ticket.isPagado() != pagadoFinal) {
            changes.append("pagado: ").append(ticket.isPagado()).append(" -> ").append(pagadoFinal).append("; ");
            ticket.setPagado(pagadoFinal);
        }

        Ticket saved = ticketRepository.save(ticket);
        if (tecnico != null && (oldTecnico == null || !oldTecnico.equals(tecnico))) {
            notificationService.notifyTicketAssigned(tecnico, saved);
        }
        saveHistory(saved, "UPDATED", previousStatus, saved.getStatus(), changes.toString());
        return saved;
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
    public Ticket actualizaTicketPorStatus(TicketStatus status, long id, String codigoTecnico){
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        if(ticket.getTecnico() == null || !ticket.getTecnico().getCodigo().equals(codigoTecnico)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        TicketStatus previous = ticket.getStatus();
        ticket.setStatus(status);
        Ticket saved = ticketRepository.save(ticket);
        saveHistory(saved, "STATUS_CHANGED", previous, saved.getStatus(), "status cambiado");
        return saved;
    }

    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        if (!ticket.isDeleted()) {
            ticket.setDeleted(true);
            Ticket saved = ticketRepository.save(ticket);
            saveHistory(saved, "DELETED", saved.getStatus(), saved.getStatus(), null);
        }
    }

    public Ticket restoreTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        if (ticket.isDeleted()) {
            ticket.setDeleted(false);
            Ticket saved = ticketRepository.save(ticket);
            saveHistory(saved, "RESTORED", saved.getStatus(), saved.getStatus(), null);
            return saved;
        }
        return ticket;
    }

    public List<TicketHistory> getHistory(Long ticketId) {
        return historyRepository.findByTicketIdOrderByTimestampAsc(ticketId);
    }

    public Ticket finalizarConReporte(Long id, String reporte, MultipartFile[] fotos) throws IOException{
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        ticket.setStatus(TicketStatus.FINALIZADO);
        ticket.setReporteServicio(reporte);

        if (fotos != null && fotos.length > 0) {
            Path folderPath = Paths.get(System.getProperty("user.home"), "enset-data", "reporte-fotos");
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            StringBuilder paths = new StringBuilder();
            for (MultipartFile foto : fotos) {
                if (foto != null && !foto.isEmpty()) {
                    String fileName = UUID.randomUUID().toString();
                    Path filePath = folderPath.resolve(fileName + "-" + foto.getOriginalFilename());
                    Files.copy(foto.getInputStream(), filePath);
                    if (paths.length() > 0) paths.append(",");
                    paths.append(filePath.toUri().toString());
                }
            }
            ticket.setReporteFotos(paths.toString());
        }

        Ticket saved = ticketRepository.save(ticket);
        notificationService.notifyTicketFinished(saved);
        return saved;
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

    public AdminDashboardDto getAdminStats() {
        return new AdminDashboardDto(
            ticketRepository.countByPagadoTrueAndDeletedFalse(),
            ticketRepository.countByPagadoFalseAndDeletedFalse(),
            ticketRepository.countByStatusAndPagadoFalseAndDeletedFalse(TicketStatus.FINALIZADO),
            ticketRepository.countByStatusAndPagadoTrueAndDeletedFalse(TicketStatus.FINALIZADO),
            ticketRepository.countByFechaAndDeletedFalse(LocalDate.now()),
            ticketRepository.countByStatusAndDeletedFalse(TicketStatus.PENDIENTE),
            ticketRepository.countByStatusAndDeletedFalse(TicketStatus.EN_PROCESO),
            ticketRepository.countByStatusAndDeletedFalse(TicketStatus.FINALIZADO)
        );
    }

    private boolean equalsStr(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}

package com.compulandia.sistematickets.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import com.compulandia.sistematickets.dto.TecnicoRegistroDto;
import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.entities.Usuario;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.ServicioRepository;
import com.compulandia.sistematickets.repository.UsuarioRepository;
import com.compulandia.sistematickets.repository.TicketRepository;
import com.compulandia.sistematickets.repository.NotificationRepository;

@RestController
@CrossOrigin("*")
public class TecnicoController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/tecnicos")
    @Transactional
    public Tecnico saveTecnico(@RequestBody TecnicoRegistroDto dto) {
        Tecnico tecnico = Tecnico.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .codigo(dto.getCodigo())
                .email(dto.getEmail())
                .especialidades(dto.getEspecialidades())
                .build();

        if (tecnico.getEspecialidades() != null) {
            tecnico.setEspecialidades(
                tecnico.getEspecialidades().stream()
                    .map(s -> {
                        Servicio existing = servicioRepository.findByNombre(s.getNombre());
                        return existing != null ? existing : servicioRepository.save(s);
                    })
                    .toList());
        }

        Tecnico savedTecnico = tecnicoRepository.save(tecnico);

        if (dto.getUsername() != null && dto.getPassword() != null) {
            Usuario usuario = Usuario.builder()
                    .username(dto.getUsername())
                    .password(dto.getPassword())
                    .role("TECNICO")
                    .codigoTecnico(dto.getCodigo())
                    .build();
            usuarioRepository.save(usuario);
        }

        return savedTecnico;
    }

    @GetMapping("/tecnicos/proximoCodigo")
    public String obtenerProximoCodigo() {
        long count = tecnicoRepository.count() + 1;
        return String.format("TE-%03d", count);
    }

    @PutMapping("/tecnicos/{codigo}")
    @Transactional
    public Tecnico actualizarTecnico(@PathVariable String codigo, @RequestBody TecnicoRegistroDto dto) {
        Tecnico tecnico = tecnicoRepository.findByCodigo(codigo);
        if (tecnico == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tecnico no encontrado");
        }

        // Validar cambio de codigo
        if (dto.getCodigo() != null && !dto.getCodigo().equals(codigo)) {
            Tecnico exist = tecnicoRepository.findByCodigo(dto.getCodigo());
            if (exist != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Codigo de tecnico ya existe");
            }
            tecnico.setCodigo(dto.getCodigo());
        }

        if (dto.getNombre() != null) {
            tecnico.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            tecnico.setApellido(dto.getApellido());
        }
        if (dto.getEmail() != null) {
            tecnico.setEmail(dto.getEmail());
        }

        if (dto.getEspecialidades() != null) {
            tecnico.setEspecialidades(
                dto.getEspecialidades().stream()
                    .map(s -> {
                        Servicio existing = servicioRepository.findByNombre(s.getNombre());
                        return existing != null ? existing : servicioRepository.save(s);
                    })
                    .toList());
        }

        // Actualizar datos de usuario asociado
        Usuario usuario = usuarioRepository.findByCodigoTecnico(codigo);
        if (usuario != null) {
            boolean usernameChange = dto.getUsername() != null && !dto.getUsername().isEmpty()
                    && !dto.getUsername().equals(usuario.getUsername());

            // Si el username cambia, eliminar el antiguo y crear uno nuevo
            if (usernameChange) {
                if (usuarioRepository.existsById(dto.getUsername())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ya existe");
                }
                Usuario nuevoUsuario = Usuario.builder()
                        .username(dto.getUsername())
                        .password(dto.getPassword() != null ? dto.getPassword() : usuario.getPassword())
                        .role(usuario.getRole())
                        .codigoTecnico(dto.getCodigo() != null ? dto.getCodigo() : codigo)
                        .servicios(usuario.getServicios())
                        .build();
                usuarioRepository.delete(usuario);
                usuarioRepository.save(nuevoUsuario);
            } else {
                if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                    usuario.setPassword(dto.getPassword());
                }
                if (dto.getCodigo() != null) {
                    usuario.setCodigoTecnico(dto.getCodigo());
                }
                usuarioRepository.save(usuario);
            }
        } else if (dto.getUsername() != null && dto.getPassword() != null) {
            if (usuarioRepository.existsById(dto.getUsername())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ya existe");
            }
            usuarioRepository.save(Usuario.builder()
                    .username(dto.getUsername())
                    .password(dto.getPassword())
                    .role("TECNICO")
                    .codigoTecnico(dto.getCodigo() != null ? dto.getCodigo() : codigo)
                    .build());
        }

        return tecnicoRepository.save(tecnico);
    }

    @DeleteMapping("/tecnicos/{codigo}")
    @Transactional
    public void eliminarTecnico(@PathVariable String codigo) {
        Tecnico tecnico = tecnicoRepository.findByCodigo(codigo);
        if (tecnico == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tecnico no encontrado");
        }

        // Eliminar usuario asociado si existe
        Usuario usuario = usuarioRepository.findByCodigoTecnico(codigo);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }

        // Desvincular el técnico de los tickets activos
        ticketRepository.findByTecnicoCodigoAndDeletedFalse(codigo)
                .forEach(t -> {
                    t.setTecnico(null);
                    ticketRepository.save(t);
                });

        // Eliminar notificaciones asociadas al técnico
        notificationRepository.deleteAll(notificationRepository.findByTecnicoCodigoOrderByCreatedAtDesc(codigo));

        // Finalmente eliminar el técnico
        tecnicoRepository.delete(tecnico);
    }
}

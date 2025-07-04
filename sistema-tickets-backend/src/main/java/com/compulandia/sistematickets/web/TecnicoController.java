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

@RestController
@CrossOrigin("*")
public class TecnicoController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/tecnicos")
    @Transactional
    public Tecnico saveTecnico(@RequestBody TecnicoRegistroDto dto) {
        Tecnico tecnico = Tecnico.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .codigo(dto.getCodigo())
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
            if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
                usuario.setUsername(dto.getUsername());
            }
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                usuario.setPassword(dto.getPassword());
            }
            if (dto.getCodigo() != null) {
                usuario.setCodigoTecnico(dto.getCodigo());
            }
            usuarioRepository.save(usuario);
        } else if (dto.getUsername() != null && dto.getPassword() != null) {
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
    public void eliminarTecnico(@PathVariable String codigo) {
        Tecnico tecnico = tecnicoRepository.findByCodigo(codigo);
        if (tecnico == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tecnico no encontrado");
        }
        tecnicoRepository.delete(tecnico);
    }
}

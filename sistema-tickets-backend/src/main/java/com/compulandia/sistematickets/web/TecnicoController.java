package com.compulandia.sistematickets.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
}

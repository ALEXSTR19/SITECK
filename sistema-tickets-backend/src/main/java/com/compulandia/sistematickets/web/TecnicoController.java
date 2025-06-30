package com.compulandia.sistematickets.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.transaction.Transactional;

import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.ServicioRepository;

@RestController
@CrossOrigin("*")
public class TecnicoController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @PostMapping("/tecnicos")
    @Transactional
    public Tecnico saveTecnico(@RequestBody Tecnico tecnico) {
        if (tecnico.getEspecialidades() != null) {
            tecnico.setEspecialidades(
                tecnico.getEspecialidades().stream()
                    .map(s -> {
                        Servicio existing = servicioRepository.findByNombre(s.getNombre());
                        return existing != null ? existing : servicioRepository.save(s);
                    })
                    .toList());
        }
        return tecnicoRepository.save(tecnico);
    }

    @GetMapping("/tecnicos/proximoCodigo")
    public String obtenerProximoCodigo() {
        long count = tecnicoRepository.count() + 1;
        return String.format("TE-%03d", count);
    }
}

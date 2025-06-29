package com.compulandia.sistematickets.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.repository.TecnicoRepository;

@RestController
@CrossOrigin("*")
public class TecnicoController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @PostMapping("/tecnicos")
    public Tecnico saveTecnico(@RequestBody Tecnico tecnico) {
        if (tecnico.getId() == null || tecnico.getId().isBlank()) {
            tecnico.setId(UUID.randomUUID().toString());
        }
        return tecnicoRepository.save(tecnico);
    }

    @GetMapping("/tecnicos/proximoCodigo")
    public String obtenerProximoCodigo() {
        long count = tecnicoRepository.count() + 1;
        return String.format("TE-%03d", count);
    }
}

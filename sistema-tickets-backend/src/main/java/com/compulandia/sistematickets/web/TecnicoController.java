package com.compulandia.sistematickets.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.repository.TecnicoRepository;

@RestController
@CrossOrigin("*")
public class TecnicoController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @PostMapping("/tecnicos")
    public Tecnico saveTecnico(@RequestBody Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }
}

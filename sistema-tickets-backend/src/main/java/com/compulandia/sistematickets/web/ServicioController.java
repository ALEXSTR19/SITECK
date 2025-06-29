package com.compulandia.sistematickets.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.repository.ServicioRepository;

@RestController
@CrossOrigin("*")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping("/servicios")
    public List<Servicio> getServicios(){
        return servicioRepository.findAll();
    }

    @PostMapping("/servicios")
    public Servicio createServicio(@RequestBody Servicio servicio){
        return servicioRepository.save(servicio);
    }
}

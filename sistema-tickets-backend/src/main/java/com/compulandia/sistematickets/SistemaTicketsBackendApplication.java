package com.compulandia.sistematickets;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.compulandia.sistematickets.entities.Usuario;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.TicketRepository;
import com.compulandia.sistematickets.repository.ServicioRepository;
import com.compulandia.sistematickets.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class SistemaTicketsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaTicketsBackendApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner initData(TecnicoRepository tecnicoRepository,
            TicketRepository ticketRepository,
            ServicioRepository servicioRepository,
            UsuarioRepository usuarioRepository) {
        return args -> {
            // Registrar servicios oficiales
            if (servicioRepository.findByNombre("MANTENIMIENTO DE EQUIPOS CORRECTIVO Y PREVENTIVO") == null) {
                servicioRepository.save(Servicio.builder()
                        .nombre("MANTENIMIENTO DE EQUIPOS CORRECTIVO Y PREVENTIVO")
                        .build());
            }
            if (servicioRepository.findByNombre("DESARROLLO BACKEND") == null) {
                servicioRepository.save(Servicio.builder()
                        .nombre("DESARROLLO BACKEND")
                        .build());
            }

            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(Usuario.builder()
                    .username("admin")
                    .password("admin")
                    .role("ADMIN")
                    .build());
            }

            // Previously the database was cleared on each application start.
            // This caused tickets and technicians to disappear after every restart,
            // preventing them from being listed through the API.
            // Those delete calls have been removed to preserve the data.
        };
    }
}

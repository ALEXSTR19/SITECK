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

import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.entities.Usuario;
import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;
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
            // Verificar si ya existen datos para no duplicar
            if (tecnicoRepository.count() == 0) {
                List<Tecnico> tecnicos = Arrays.asList(
                    Tecnico.builder()
                        .nombre("Alexis")
                        .apellido("Gonzalez")
                        .codigo("TE-001")
                        .especialidades(Arrays.asList(
                            Servicio.builder().nombre("Desarrollo Backend").build()
                        ))
                        .build()
                    // ... otros técnicos
                );
                
                tecnicoRepository.saveAll(tecnicos);

                // Insertar tickets solo si no existen
                if (ticketRepository.count() == 0) {
                    TypeTicket[] tiposTicket = TypeTicket.values();
                    Random random = new Random();
                    List<Ticket> tickets = new ArrayList<>();
                    
                    for (Tecnico tecnico : tecnicoRepository.findAll()) {
                        for (int i = 0; i < 10; i++) {
                            tickets.add(Ticket.builder()
                                .cantidad(1000 + random.nextInt(20000))
                                .type(tiposTicket[random.nextInt(tiposTicket.length)])
                                .status(TicketStatus.PENDIENTE)
                                .fecha(LocalDate.now())
                                .tecnico(tecnico)
                                .build());
                        }
                    }
                    ticketRepository.saveAll(tickets);
                }
            }
        };
    }
}

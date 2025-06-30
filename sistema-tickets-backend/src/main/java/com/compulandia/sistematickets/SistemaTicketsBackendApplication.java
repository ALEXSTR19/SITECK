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

import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.entities.Servicio;
import com.compulandia.sistematickets.entities.Usuario;
import com.compulandia.sistematickets.enums.TicketStatus;
import com.compulandia.sistematickets.enums.TypeTicket;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.TicketRepository;
import com.compulandia.sistematickets.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class SistemaTicketsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaTicketsBackendApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner initData(TecnicoRepository tecnicoRepository, TicketRepository ticketRepository, UsuarioRepository usuarioRepository) {
        return args -> {
                        if (usuarioRepository.count() == 0) {
                            usuarioRepository.save(Usuario.builder()
                                .username("admin")
                                .password("admin")
                                .role("ADMIN")
                                .build());
                        }
                    };
                }
            }
    

package com.compulandia.sistematickets.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compulandia.sistematickets.entities.Rating;
import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.repository.RatingRepository;
import com.compulandia.sistematickets.repository.TecnicoRepository;
import com.compulandia.sistematickets.repository.TicketRepository;

@RestController
@CrossOrigin("*")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping("/ratings")
    public Rating saveRating(@RequestParam int stars,
                             @RequestParam String comment,
                             @RequestParam Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        Tecnico tecnico = ticket.getTecnico();
        Rating rating = Rating.builder()
                .stars(stars)
                .comment(comment)
                .ticket(ticket)
                .tecnico(tecnico)
                .build();
        return ratingRepository.save(rating);
    }
}

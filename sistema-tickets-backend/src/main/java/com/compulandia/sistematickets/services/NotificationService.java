package com.compulandia.sistematickets.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compulandia.sistematickets.entities.Notification;
import com.compulandia.sistematickets.entities.Tecnico;
import com.compulandia.sistematickets.entities.Ticket;
import com.compulandia.sistematickets.repository.NotificationRepository;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private NotificationRepository repository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void notifyTicketAssigned(Tecnico tecnico, Ticket ticket) {
        if (tecnico == null || ticket == null) return;
        Notification notification = Notification.builder()
            .tecnico(tecnico)
            .ticket(ticket)
            .message("Se te ha asignado el ticket #" + ticket.getId())
            .createdAt(LocalDateTime.now())
            .build();
        repository.save(notification);

        if (mailSender != null && tecnico.getEmail() != null && !tecnico.getEmail().isEmpty()) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(tecnico.getEmail());
            msg.setSubject("Nuevo ticket asignado");
            msg.setText("Se te ha asignado el ticket #" + ticket.getId());
            try {
                mailSender.send(msg);
            } catch (Exception e) {
                log.warn("Failed to send email notification", e);
            }
        }
    }
}

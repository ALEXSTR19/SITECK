package com.compulandia.sistematickets.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${admin.notification.email:}")
    private String adminEmail;

    public void notifyTicketAssigned(Tecnico tecnico, Ticket ticket) {
        if (tecnico == null || ticket == null) return;
        Notification notification = Notification.builder()
            .tecnico(tecnico)
            .ticket(ticket)
            .message("Se te ha asignado el ticket #" + ticket.getId())
            .createdAt(LocalDateTime.now())
            .build();
        repository.save(notification);

        if (mailSender == null) {
            log.warn("JavaMailSender bean not configured, cannot send email notification");
            return;
        }

        if (tecnico.getEmail() == null || tecnico.getEmail().isEmpty()) {
            log.warn("Technician '{}' does not have an email configured", tecnico.getCodigo());
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        if (fromEmail != null && !fromEmail.isEmpty()) {
            msg.setFrom(fromEmail);
        } else {
            log.info("No 'from' email configured, using default configuration");
        }
        msg.setTo(tecnico.getEmail());
        msg.setSubject("Nuevo ticket asignado");
        msg.setText("Se te ha asignado el ticket #" + ticket.getId());
        try {
            mailSender.send(msg);
            log.info("Email notification sent to {}", tecnico.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send email notification", e);
        }
    }

    public void notifyTicketFinished(Ticket ticket) {
        if (ticket == null) return;

        if (mailSender == null) {
            log.warn("JavaMailSender bean not configured, cannot send email notification");
            return;
        }

        if (adminEmail == null || adminEmail.isEmpty()) {
            log.warn("Admin email not configured, cannot send notification");
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        if (fromEmail != null && !fromEmail.isEmpty()) {
            msg.setFrom(fromEmail);
        } else {
            log.info("No 'from' email configured, using default configuration");
        }
        msg.setTo(adminEmail);
        msg.setSubject("Ticket finalizado");

        String tecnicoNombre = ticket.getTecnico() != null ? ticket.getTecnico().getNombre() : "Un tecnico";
        msg.setText(tecnicoNombre + " ha finalizado el ticket #" + ticket.getId());

        try {
            mailSender.send(msg);
            log.info("Email notification sent to admin {}", adminEmail);
        } catch (Exception e) {
            log.warn("Failed to send email notification to admin", e);
        }
    }
}

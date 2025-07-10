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

    @Value("${client.feedback.url:http://localhost:8080/feedback.html}")
    private String feedbackUrl;

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
        msg.setSubject("Asignación de Ticket #" + ticket.getId());

        String servicio = ticket.getServicio() != null ? ticket.getServicio().getNombre() : "N/A";
        String prioridad = ticket.getPriority() != null ? ticket.getPriority().name() : "N/A";
        String cliente = "";
        if (ticket.getCliente() != null) {
            cliente = ticket.getCliente().getNombre();
            if (ticket.getCliente().getApellido() != null) {
                cliente += " " + ticket.getCliente().getApellido();
            }
        } else {
            cliente = "N/A";
        }
        StringBuilder body = new StringBuilder();
        body.append("Hola ")
            .append(tecnico.getNombre() != null ? tecnico.getNombre() : "")
            .append(',').append("\n\n")
            .append("Se te ha asignado el ticket #")
            .append(ticket.getId())
            .append(" correspondiente al servicio ")
            .append(servicio)
            .append('.').append("\n")
            .append("Prioridad: ")
            .append(prioridad)
            .append("\n")
            .append("Cliente: ")
            .append(cliente)
            .append("\n")
            .append("Fecha: ")
            .append(ticket.getFecha())
            .append("\n\nPor favor ingresa al sistema para revisar los detalles.")
            .append("\n\n--\nSistema de Tickets");

        msg.setText(body.toString());
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

        if (ticket.getCliente() != null && ticket.getCliente().getEmail() != null
                && !ticket.getCliente().getEmail().isEmpty()) {
            SimpleMailMessage clientMsg = new SimpleMailMessage();
            if (fromEmail != null && !fromEmail.isEmpty()) {
                clientMsg.setFrom(fromEmail);
            }
            clientMsg.setTo(ticket.getCliente().getEmail());
            clientMsg.setSubject("Califica nuestro servicio");
            String url = feedbackUrl + "?ticketId=" + ticket.getId();
            clientMsg.setText("Su servicio ha sido finalizado. Por favor valore el servicio en el siguiente enlace:\n" + url);
            try {
                mailSender.send(clientMsg);
                log.info("Feedback email sent to {}", ticket.getCliente().getEmail());
            } catch (Exception e) {
                log.warn("Failed to send feedback email to client", e);
            }
        }
    }
}

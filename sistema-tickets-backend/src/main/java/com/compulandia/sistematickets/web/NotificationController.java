package com.compulandia.sistematickets.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compulandia.sistematickets.entities.Notification;
import com.compulandia.sistematickets.repository.NotificationRepository;

@RestController
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    private NotificationRepository repository;

    @GetMapping("/tecnicos/{codigo}/notifications")
    public List<Notification> getNotifications(@PathVariable String codigo) {
        return repository.findByTecnicoCodigoOrderByCreatedAtDesc(codigo);
    }

    @PutMapping("/notifications/{id}/read")
    public Notification markAsRead(@PathVariable Long id) {
        Notification n = repository.findById(id).orElseThrow();
        n.setRead(true);
        return repository.save(n);
    }
}

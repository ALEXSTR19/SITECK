package com.compulandia.sistematickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.compulandia.sistematickets.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTecnicoCodigoOrderByCreatedAtDesc(String codigo);
}

package com.app.tradogt.services;

import com.app.tradogt.entity.Notificacion;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Usuario;
import com.app.tradogt.repository.NotificacionRepository;
import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private Pusher pusher;
    @Autowired
    private NotificacionRepository notificacionRepository;

    // Crear y enviar una notificación en tiempo real
    public void createNotification(String message, Usuario usuario, Orden orden) {
        // Crear y guardar la notificación en la base de datos
        Notificacion notificacion = new Notificacion();
        notificacion.setContenido(message);
        notificacion.setUsuario(usuario);
        notificacion.setOrden(orden);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setLeido(false);  // La notificación es no leída por defecto
        notificacionRepository.save(notificacion);

        // Enviar la notificación en tiempo real a través de Pusher
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("message", message);
        notificationData.put("orderId", orden.getCodigo()); // Suponiendo que `codigo` es el identificador de la orden
        notificationData.put("userId", usuario.getId().toString()); // Incluye el ID del usuario

        pusher.trigger("ordenes-" + usuario.getId(), "orden-actualizada", notificationData);
    }

    // Obtener todas las notificaciones no leídas para un usuario específico
    public List<Notificacion> getUnreadNotifications(Usuario usuario) {
        return notificacionRepository.findByUsuarioAndLeidoFalseOrderByFechaCreacionDesc(usuario);
    }

    // Marcar una notificación como leída
    public void markAsRead(Integer notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId).orElse(null);
        if (notificacion != null) {
            notificacion.setLeido(true);
            notificacionRepository.save(notificacion);
        }
    }

    // Obtener todas las notificaciones para un usuario específico
    public List<Notificacion> getAllNotifications(Usuario usuario) {
        return notificacionRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }
}

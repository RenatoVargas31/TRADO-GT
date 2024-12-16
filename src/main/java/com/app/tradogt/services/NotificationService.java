package com.app.tradogt.services;

import com.app.tradogt.entity.*;
import com.app.tradogt.repository.NotificacionRepository;
import com.app.tradogt.repository.RolRepository;
import com.app.tradogt.repository.UsuarioRepository;
import com.app.tradogt.repository.ZonaRepository;
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
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private ZonaRepository zonaRepository;


    // Crear y enviar una notificación en tiempo real
    //Esta notificación indicará el cambio de estado de la respectiva orden
    public void orderChangeNotification(String message, Usuario usuario, Orden orden) {
        Notificacion notificacion = new Notificacion();
        notificacion.setContenido(message);
        notificacion.setUsuario(usuario);
        notificacion.setOrden(orden);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setLeido(false);  // no leída por defecto
        notificacionRepository.save(notificacion);

        String imageUrl;
        switch (orden.getEstadoordenIdestadoorden().getId()) {
            case 4:
                imageUrl = "/images/notiIcons/orden-arriboPais-svgrepo-com.svg";
                break;
            case 5:
                imageUrl = "/images/notiIcons/orden-aduanas-svgrepo-com.svg";
                break;
            case 6:
                imageUrl = "/images/notiIcons/orden-enCamino-svgrepo-com.svg";
                break;
            case 7:
                imageUrl = "/images/notiIcons/orden-recibida-svgrepo-com.svg";
                break;
            default:
                imageUrl = "/images/notiIcons/order-svgrepo-com.svg";
        }

        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("message", message);
        notificationData.put("orderId", orden.getCodigo());
        notificationData.put("userId", usuario.getId().toString());
        notificationData.put("imageUrl", imageUrl);
        notificationData.put("orderIdReal", orden.getId().toString());
        // Nueva línea: estado de la notificación
        notificationData.put("estaUnread", notificacion.getLeido()==true ? "read" : "unread");

        pusher.trigger("ordenes-" + usuario.getId(), "orden-actualizada", notificationData);
    }


    // Obtener todas las notificaciones no leídas para un usuario específico
    public List<Notificacion> getUnreadNotifications(Usuario usuario) {

        return notificacionRepository.findByUsuarioOrderByFechaCreacionAsc(usuario);

    }

    //Notificación que indicará cuando exista bajo stock y cuando exista stock adecuado
    public void stockNotification(String message, Zona zona) {

        // Obtener todos los administradores zonales de la zona
        List<Usuario> administradoresZonales = usuarioRepository.findByRolIdrolAndZonaIdzona(rolRepository.findById(2).get(),zona );

        // Crear y guardar notificaciones individuales para cada administrador zonal
        for (Usuario usuario : administradoresZonales) {
            Notificacion notificacion = new Notificacion();
            notificacion.setContenido(message);
            notificacion.setUsuario(usuario);
            notificacion.setFechaCreacion(LocalDateTime.now());
            notificacion.setLeido(false);
            notificacionRepository.save(notificacion);
        }

        // Crear el payload para el evento
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("message", message);
        notificationData.put("zonaId", zona.getId().toString());
        notificationData.put("rol", rolRepository.findById(2).get().getNombre());

        // Usar un canal basado en el rol y la zona
        pusher.trigger("stock-admin-zona-" + zona.getId(), "reposicion-stock", notificationData);
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

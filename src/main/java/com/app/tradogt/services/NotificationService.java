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
        //System.out.println("Creando notificación para usuario ID: " + usuario.getId() + " con mensaje: " + message);

        // Crear y guardar la notificación en la base de datos
        Notificacion notificacion = new Notificacion();
        notificacion.setContenido(message);
        notificacion.setUsuario(usuario);
        notificacion.setOrden(orden);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setLeido(false);  // La notificación es no leída por defecto
        notificacionRepository.save(notificacion);
        //System.out.println("GUARDADO - Notificación AGENTE guardada. ID: " + notificacion.getIdNoti());


        // Seleccionar una imagen dependiendo del estado de la orden
        String imageUrl;
        switch (orden.getEstadoordenIdestadoorden().getId()) {
            case 4: // Arribo al país
                imageUrl = "/images/notiIcons/orden-arriboPais-svgrepo-com.svg";
                break;
            case 5: // En aduanas
                imageUrl = "/images/notiIcons/orden-aduanas-svgrepo-com.svg";
                break;
            case 6: // En ruta
                imageUrl = "/images/notiIcons/orden-enCamino-svgrepo-com.svg";
                break;
            case 7: // Recibido
                imageUrl = "/images/notiIcons/orden-recibida-svgrepo-com.svg";
                break;
            default:
                imageUrl = "/images/notiIcons/order-svgrepo-com.svg";
        }

        // Enviar la notificación en tiempo real a través de Pusher
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("message", message);
        notificationData.put("orderId", orden.getCodigo()); // Suponiendo que `codigo` es el identificador de la orden
        notificationData.put("userId", usuario.getId().toString()); // Incluye el ID del usuario
        notificationData.put("imageUrl", imageUrl); // Agrega la URL de la imagen
        notificationData.put("orderIdReal", orden.getId().toString());

        pusher.trigger("ordenes-" + usuario.getId(), "orden-actualizada", notificationData);
        //System.out.println("Enviando notificación: " + message + " al canal ordenes-" + usuario.getId());


    }

    // Obtener todas las notificaciones no leídas para un usuario específico
    public List<Notificacion> getUnreadNotifications(Usuario usuario) {

        return notificacionRepository.findByUsuarioAndLeidoFalseOrderByFechaCreacionAsc(usuario);

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

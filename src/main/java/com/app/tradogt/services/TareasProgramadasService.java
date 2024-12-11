package com.app.tradogt.services;

import com.app.tradogt.entity.EstadoOrden;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.repository.EstadoOrdenRepository;
import com.app.tradogt.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//Este service utiliza Scheduled con el objetivo de ejecutar tareas sin necesidad de ser invocadas
//Se establece cada caunto se ejecutará
@Service
public class TareasProgramadasService {

    @Autowired
    EstadoOrdenRepository estadoOrdenRepository;

    @Autowired
    OrdenRepository ordenRepository;

    @Autowired
    NotificationService notificationService;

    @Scheduled(cron = "0 0/5 * * * *") // Ejecuta cada 5 minutos
    public void updateOrderStatus() {
        // Obtener la fecha actual
        LocalDate today = LocalDate.now();
        System.out.println("Actualizando estados de órdenes. Fecha actual: " + today);

        // Recuperar los estados correspondientes (3 al 6)
        Optional<EstadoOrden> estadoEnProceso = estadoOrdenRepository.findById(3);
        Optional<EstadoOrden> estadoArriboAlPais = estadoOrdenRepository.findById(4);
        Optional<EstadoOrden> estadoEnAduanas = estadoOrdenRepository.findById(5);
        Optional<EstadoOrden> estadoEnRuta = estadoOrdenRepository.findById(6);
        Optional<EstadoOrden> estadoRecibido = estadoOrdenRepository.findById(7);

        // Obtener órdenes con estado entre 3 y 6
        List<Orden> orders = ordenRepository.findByEstadoordenIdestadoordenIn(List.of(
                estadoEnProceso.get(),
                estadoArriboAlPais.get(),
                estadoEnAduanas.get(),
                estadoEnRuta.get()
        ));

        // Iterar sobre las órdenes y actualizar el estado según las fechas
        for (Orden orden : orders) {
            try {
                System.out.println("Procesando Orden ID: " + orden.getId());

                if (orden.getFechaArribo() != null && orden.getFechaArribo().isEqual(today)) {
                    orden.setEstadoordenIdestadoorden(estadoArriboAlPais.get());
                    // Enviamos notificación a agente
                    String messagetoAgente = "La orden #" + orden.getCodigo() + " ha llegado al país.";
                    String messagetoUser = "Tu orden #" + orden.getCodigo() + " ha llegado al país.";
                    notificationService.orderChangeNotification(messagetoAgente, orden.getAgentcompraIdusuario(), orden);
                    notificationService.orderChangeNotification(messagetoUser, orden.getUsuarioIdusuario(), orden);
                } else if (orden.getFechaEnAduanas() != null && orden.getFechaEnAduanas().isEqual(today)) {
                    orden.setEstadoordenIdestadoorden(estadoEnAduanas.get());
                    // Enviamos notificación a agente
                    String messagetoAgente = "La orden #" + orden.getCodigo() + " está actualmente en aduanas.";
                    String messagetoUser = "Tu orden #" + orden.getCodigo() + " está actualmente en aduanas.";
                    notificationService.orderChangeNotification(messagetoAgente, orden.getAgentcompraIdusuario(), orden);
                    notificationService.orderChangeNotification(messagetoUser, orden.getUsuarioIdusuario(), orden);
                } else if (orden.getFechaEnRuta() != null && orden.getFechaEnRuta().isEqual(today)) {
                    orden.setEstadoordenIdestadoorden(estadoEnRuta.get());
                    // Enviamos notificación a agente
                    String messagetoAgente = "La orden #" + orden.getCodigo() + " está en ruta hacia el destino.";
                    String messagetoUser = "Tu orden #" + orden.getCodigo() + " está en ruta hacia el destino.";
                    notificationService.orderChangeNotification(messagetoAgente, orden.getAgentcompraIdusuario(), orden);
                    notificationService.orderChangeNotification(messagetoUser, orden.getUsuarioIdusuario(), orden);
                } else if (orden.getFechaRecibido() != null && orden.getFechaRecibido().isEqual(today)) {
                    orden.setEstadoordenIdestadoorden(estadoRecibido.get());
                    // Enviamos notificación a agente
                    System.out.println("ocurre esto xddddd (ojo aqui)");
                    String messagetoAgente = "La orden #" + orden.getCodigo() + " ha sido recibida.";
                    String messagetoUser = "Tu orden #" + orden.getCodigo() + " ha sido recibida.";
                    notificationService.orderChangeNotification(messagetoAgente, orden.getAgentcompraIdusuario(), orden);
                    notificationService.orderChangeNotification(messagetoUser, orden.getUsuarioIdusuario(), orden);
                }

                // Guardar la orden actualizada
                ordenRepository.save(orden);
            } catch (Exception e) {
                System.err.println("Error al procesar la Orden ID: " + orden.getId() + " - " + e.getMessage());
            }
        }

    }
}

package com.app.tradogt.services;

import com.app.tradogt.entity.EstadoOrden;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.repository.EstadoOrdenRepository;
import com.app.tradogt.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrackingDateAndStatus {

    @Autowired
    final OrdenRepository ordenRepository;
    final EstadoOrdenRepository estadoOrdenRepository;

    public TrackingDateAndStatus(OrdenRepository ordenRepository, EstadoOrdenRepository estadoOrdenRepository) {
        this.ordenRepository = ordenRepository;
        this.estadoOrdenRepository = estadoOrdenRepository;
    }

    //findByFechaEnProcesoAndEstadoordenIdestadoorden

    @Transactional
    public void updateOrderStatus() {
        //Obtener la fecha actual
        LocalDate today = LocalDate.now();

        Optional<EstadoOrden> estadoactual = estadoOrdenRepository.findById(3);
        Optional<EstadoOrden> arriboAlPais = estadoOrdenRepository.findById(4);
        Optional<EstadoOrden> aduanas = estadoOrdenRepository.findById(5);
        Optional<EstadoOrden> ruta = estadoOrdenRepository.findById(6);
        Optional<EstadoOrden> recibido = estadoOrdenRepository.findById(7);

        List<Orden> ordensInProcess = ordenRepository.findByEstadoordenIdestadoorden(estadoactual);

        for (Orden orden : ordensInProcess) {
            System.out.println("orden: " + orden.getId());

            ordenRepository.save(orden);
        }
    }
}

package com.app.tradogt.restController;

import com.app.tradogt.dto.OrdenEstadoDto;
import com.app.tradogt.dto.ProductoMasVendidoDto;
import com.app.tradogt.dto.UsuarioMesDto;
import com.app.tradogt.repository.OrdenRepository;
import com.app.tradogt.repository.ProductosRepository;
import com.app.tradogt.repository.UsuarioRepository;
import jakarta.validation.constraints.Null;
import org.springframework.data.util.NullableUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/superAdmin")
public class SuperAdminRest {
    final UsuarioRepository usuarioRepository;
    final OrdenRepository ordenRepository;
    final ProductosRepository productosRepository;

    public SuperAdminRest(UsuarioRepository usuarioRepository, OrdenRepository ordenRepository, ProductosRepository productosRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ordenRepository = ordenRepository;
        this.productosRepository = productosRepository;
    }

    @GetMapping("UsuariosMes")
    public Map<String, Object> getUsuariosMes(){
        List<UsuarioMesDto> usuariosRegistrados = usuarioRepository.getUsuariosRegistrados();
        //Contar cuántos usuarios hay en cada mes y almacenar en una lista los resultados por mes
        List<Integer> usuariosRegistradosPorMes = new ArrayList<>();
        List<Integer> usuariosActivosPorMes = new ArrayList<>();
        List<Integer> usuariosInactivosPorMes = new ArrayList<>();

        int cantidadRegistrados = 0;
        int cantidadActivos = 0;
        int cantidadInactivos = 0;

        for (int i = 1; i <= 12; i++) {
            for (UsuarioMesDto usuario : usuariosRegistrados) {
                if (usuario.getFechaRegistro().getMonthValue() == i) {
                    cantidadRegistrados = cantidadRegistrados + 1;
                    if(usuario.getIsActivated() == 1){
                        cantidadActivos = cantidadActivos + 1;
                    }else{
                        cantidadInactivos = cantidadInactivos + 1;
                    }
                }
            }
            usuariosRegistradosPorMes.add(cantidadRegistrados);
            usuariosActivosPorMes.add(cantidadActivos);
            usuariosInactivosPorMes.add(cantidadInactivos);
            cantidadRegistrados = 0;
            cantidadActivos = 0;
            cantidadInactivos = 0;
        }
        return Map.of("usuariosRegistradosPorMes", usuariosRegistradosPorMes, "usuariosActivosPorMes", usuariosActivosPorMes, "usuariosInactivosPorMes", usuariosInactivosPorMes);
    }
    @GetMapping("OrdenEstado")
    public Map<String, Object> getOrdenEstado(){
        //Lista de fechas de cada estado de la orden
        List<OrdenEstadoDto> ordenes = ordenRepository.getOrdenEstado();
        //Listas para almacenar la cantidad de órdenes en cada estado
        List<Integer> ordenesCreadas = new ArrayList<>();
        List<Integer> ordenesValidadas = new ArrayList<>();
        List<Integer> ordenesEnProceso = new ArrayList<>();
        List<Integer> ordenesArribo = new ArrayList<>();
        List<Integer> ordenesEnAduanas = new ArrayList<>();
        List<Integer> ordenesEnRuta = new ArrayList<>();
        List<Integer> ordenesRecibido = new ArrayList<>();
        //Contadores
        int cantidadCreadas = 0;
        int cantidadValidadas = 0;
        int cantidadEnProceso = 0;
        int cantidadArribo = 0;
        int cantidadEnAduanas = 0;
        int cantidadEnRuta = 0;
        int cantidadRecibido = 0;
        //Recorrer la lista de órdenes y contar cuántas hay en cada estado y almacenar por mes
        for (int i = 1; i <= 12; i++) {
            for (OrdenEstadoDto orden : ordenes) {
                if (orden.getFechaCreacion() != null && orden.getFechaCreacion().getMonthValue() == i) {
                    cantidadCreadas = cantidadCreadas + 1;
                }
                if (orden.getFechaValidacion() != null && orden.getFechaValidacion().getMonthValue() == i) {
                    cantidadValidadas = cantidadValidadas + 1;
                }
                if (orden.getFechaEnProceso() != null && orden.getFechaEnProceso().getMonthValue() == i) {
                    cantidadEnProceso = cantidadEnProceso + 1;
                }
                if (orden.getFechaArribo() != null && orden.getFechaArribo().getMonthValue() == i) {
                    cantidadArribo = cantidadArribo + 1;
                }
                if (orden.getFechaEnAduanas() != null && orden.getFechaEnAduanas().getMonthValue() == i) {
                    cantidadEnAduanas = cantidadEnAduanas + 1;
                }
                if (orden.getFechaEnRuta() != null && orden.getFechaEnRuta().getMonthValue() == i) {
                    cantidadEnRuta = cantidadEnRuta + 1;
                }
                if (orden.getFechaRecibido() != null && orden.getFechaRecibido().getMonthValue() == i) {
                    cantidadRecibido = cantidadRecibido + 1;
                }
            }
            ordenesCreadas.add(cantidadCreadas);
            ordenesValidadas.add(cantidadValidadas);
            ordenesEnProceso.add(cantidadEnProceso);
            ordenesArribo.add(cantidadArribo);
            ordenesEnAduanas.add(cantidadEnAduanas);
            ordenesEnRuta.add(cantidadEnRuta);
            ordenesRecibido.add(cantidadRecibido);
            cantidadCreadas = 0;
            cantidadValidadas = 0;
            cantidadEnProceso = 0;
            cantidadArribo = 0;
            cantidadEnAduanas = 0;
            cantidadEnRuta = 0;
            cantidadRecibido = 0;
        }
        return Map.of("ordenesCreadas", ordenesCreadas, "ordenesValidadas", ordenesValidadas, "ordenesEnProceso", ordenesEnProceso, "ordenesArribo", ordenesArribo, "ordenesEnAduanas", ordenesEnAduanas, "ordenesEnRuta", ordenesEnRuta, "ordenesRecibido", ordenesRecibido);
    }
    @GetMapping("ProductosMasVendidos")
    public Map<String, Object> getProductosMasVendidos(){
        List<ProductoMasVendidoDto> prodcutosMasVendidos = productosRepository.getProductosMasVendidos();
        //Listar nombres de productos más vendidos
        List<String> nombresProductos = new ArrayList<>();
        //Listar cantidad de productos más vendidos
        List<Long> cantidadProductos = new ArrayList<>();
        for (ProductoMasVendidoDto producto : prodcutosMasVendidos) {
            nombresProductos.add(producto.getNombre());
            cantidadProductos.add(producto.getCantidadVendida());
        }
        return Map.of("nombresProductos", nombresProductos, "cantidadProductos", cantidadProductos);
    }
}

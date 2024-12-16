package com.app.tradogt;

import com.app.tradogt.entity.*;
import com.app.tradogt.repository.CarritoRepository;
import com.app.tradogt.repository.NotificacionRepository;
import com.app.tradogt.repository.ProductoEnCarritoRepository;
import com.app.tradogt.repository.UsuarioRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    final UsuarioRepository usuarioRepository;
    final CarritoRepository carritoRepository;
    final ProductoEnCarritoRepository productoEnCarritoRepository;
    final NotificacionRepository notificacionRepository;

    public GlobalControllerAdvice(UsuarioRepository usuarioRepository, CarritoRepository carritoRepository, ProductoEnCarritoRepository productoEnCarritoRepository, NotificacionRepository notificacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.carritoRepository = carritoRepository;
        this.productoEnCarritoRepository = productoEnCarritoRepository;
        this.notificacionRepository = notificacionRepository;
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFoundException(NoResourceFoundException ex, Model model) {
        model.addAttribute("errorMessage", "La página que buscas no existe.||No se encontró el recurso solicitado.");
        return new ModelAndView("errorPage");
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Ocurrió un error inesperado.||Por favor, intenta de nuevo más tarde.");
        return new ModelAndView("errorPage");
    }


    @ModelAttribute
    private void getAuthenticatedUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String correoUsuario;
        if (principal instanceof UserDetails) {
            correoUsuario = ((UserDetails) principal).getUsername();
        } else {
            correoUsuario = principal.toString();
        }

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);

        model.addAttribute("usuarioAutenticado", usuario);
    }
    @ModelAttribute
    private void getCantidadProductosEnCarrito(Model model) {
        // Obtener la autenticación del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String correoUsuario;

        // Verificar si el usuario autenticado es una instancia de UserDetails
        if (principal instanceof UserDetails) {
            correoUsuario = ((UserDetails) principal).getUsername(); // Obtener el correo del usuario autenticado
        } else {
            correoUsuario = principal.toString();
        }

        // Buscar al usuario en la base de datos por correo
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);

        if (usuario != null) {
            // Obtener el carrito del usuario donde `isDelete = 0` (no eliminado)
            Carrito carritos = carritoRepository.findByUsuarioIdusuarioAndIsDelete(usuario, (byte) 0);

            List<ProductoEnCarrito> misProductos = productoEnCarritoRepository.findBycarritoIdcarrito(carritos);

            // Agregar la cantidad de productos en el carrito al modelo
            model.addAttribute("cantidadProductosEnCarrito", misProductos.size());
        } else {
            // Si no hay usuario autenticado o no tiene carrito, establecer la cantidad en 0
            model.addAttribute("cantidadProductosEnCarrito", 0);

        }
    }


    @ModelAttribute
    private void getCantidadNotificaciones(Model model) {
        // Obtener la autenticación del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String correoUsuario;

        // Verificar si el usuario autenticado es una instancia de UserDetails
        if (principal instanceof UserDetails) {
            correoUsuario = ((UserDetails) principal).getUsername(); // Obtener el correo del usuario autenticado
        } else {
            correoUsuario = principal.toString();
        }

        // Buscar al usuario en la base de datos por correo
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);

        if(usuario != null) {
            //Limitar las notificaciones solo para el Usuario
            List<Notificacion> lista =notificacionRepository.findByUsuarioNoti(usuario.getId());
            model.addAttribute("cantidadNotificaciones", lista.size());
            //Buscar la cantidad de notificcaiones
        }else {
            // Si no hay usuario autenticado o no tiene carrito, establecer la cantidad en 0
            model.addAttribute("cantidadNotificaciones", 0);
        }

    }
}

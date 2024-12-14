package com.app.tradogt.restController;

import com.app.tradogt.dto.ChatGPTRequestDto;
import com.app.tradogt.dto.ChatGPTResponseDto;
import com.app.tradogt.dto.MessageGPTDto;
import com.app.tradogt.dto.UpdateAddressRequestDto;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.repository.EstadoOrdenRepository;
import com.app.tradogt.repository.OrdenRepository;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/chatBotGPT")
public class ChatBotGPTController {

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Autowired
    private Cache<String, List<MessageGPTDto>> conversationCache;

    @Autowired
    private OrdenRepository ordenRepository; // Repositorio de órdenes

    @Autowired
    private EstadoOrdenRepository estadoOrdenRepository;

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam("prompt") String prompt,
                                       @RequestParam("sessionId") String sessionId,
                                       @RequestParam("orderId") String orderId){
        // Validar que el orderId exista en la base de datos
        Orden order = ordenRepository.findById(Integer.valueOf(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        // Obtener o crear el historial de conversación para la sesión
        List<MessageGPTDto> messages = conversationCache.getIfPresent(sessionId);
        if (messages == null) {
            messages = new ArrayList<>();
        }

        // Si es una nueva conversación, agregar el mensaje de sistema con detalles de la orden
        if (messages.isEmpty()) {
            String fechaRecibidoStr = (order.getFechaRecibido() != null)
                    ? order.getFechaRecibido().toString()
                    : "No hay fecha de recibido";

            String systemMessage = String.format(
                    "Eres un asistente de compras que ayuda a los usuarios con detalles de sus órdenes. " +
                            "Aquí están los detalles de la orden #%s:\n" +

                            "- Usuario: %s\n" +
                            "- Agente de compras: %s\n" +
                            "- Estado: %s\n" +
                            "- Fecha de Orden: %s\n" +
                            "- Costo Total: %s\n" +
                            "- Método de Pago: %s\n" +
                            "- Lugar de Entrega: %s\n" +
                            "- Fecha de Recibido: %s.",
                    order.getCodigo(), //String
                    order.getUsuarioIdusuario().getNombre(), //String
                    order.getAgentcompraIdusuario().getNombre(), //String
                    order.getEstadoordenIdestadoorden().getNombre(), //String
                    order.getFechaCreacion().toString(),
                    order.getCostoTotal().toString(),
                    order.getPagoIdpago().getMetodo().toString(),
                    order.getLugarEntrega(),
                    fechaRecibidoStr // Usar la variable con manejo de nulos
            );
            messages.add(new MessageGPTDto("system", systemMessage));

            // Si el estado es "EN VALIDACIÓN", agregar una instrucción adicional
            if ("EN VALIDACION".equalsIgnoreCase(order.getEstadoordenIdestadoorden().getNombre())) {
                String validationMessage = "Por favor, confirma si estás conforme con la dirección de envío proporcionada. Si deseas realizar algún cambio, indícalo.";
                messages.add(new MessageGPTDto("system", validationMessage));
            }
        }

        // Agregar el mensaje del usuario
        messages.add(new MessageGPTDto("user", prompt));

        // Crear la solicitud con todo el historial
        ChatGPTRequestDto request = new ChatGPTRequestDto(model, messages);

        try {
            // Crear la entidad sin cabeceras adicionales (ya manejadas por RestTemplate)
            HttpEntity<ChatGPTRequestDto> entity = new HttpEntity<>(request);

            // Realizar la llamada a la API de OpenAI
            ResponseEntity<ChatGPTResponseDto> chatGptResponse = template.exchange(apiURL, HttpMethod.POST, entity, ChatGPTResponseDto.class);

            if (chatGptResponse.getStatusCode() == HttpStatus.OK && chatGptResponse.getBody() != null) {
                String assistantResponse = chatGptResponse.getBody().getChoices().get(0).getMessage().getContent();

                // Agregar la respuesta del asistente al historial
                messages.add(new MessageGPTDto("assistant", assistantResponse));

                // Actualizar el historial en el cache
                conversationCache.put(sessionId, messages);

                // **Nueva Lógica:** Procesar la respuesta del usuario, no la del asistente
                handleValidationResponse(prompt, order, sessionId);

                return ResponseEntity.ok(assistantResponse);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la respuesta de OpenAI");
            }
        } catch (HttpClientErrorException.BadRequest e) {
            // Manejar específicamente el error de solicitud incorrecta
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            // Manejar otros errores de cliente
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    private void handleValidationResponse(String userResponse, Orden orden, String sessionId) {
        // Normalizar la respuesta del usuario
        String normalizedResponse = userResponse.toLowerCase().replaceAll("[áàäâ]", "a")
                .replaceAll("[éèëê]", "e")
                .replaceAll("[íìïî]", "i")
                .replaceAll("[óòöô]", "o")
                .replaceAll("[úùüû]", "u")
                .trim();

        // Verificar si el usuario confirmó la dirección
        if (normalizedResponse.equals("sí") || normalizedResponse.equals("si")) {
            // El usuario está conforme, actualizar el estado de la orden
            orden.setEstadoordenIdestadoorden(estadoOrdenRepository.findById(3).orElseThrow(() -> new IllegalArgumentException("Estado no encontrado")));
            orden.setFechaEnProceso(LocalDate.now());
            orden.setFechaArribo(LocalDate.now().plusDays(1));
            orden.setFechaEnAduanas(LocalDate.now().plusDays(2));
            orden.setFechaEnRuta(LocalDate.now().plusDays(3));
            orden.setFechaRecibido(LocalDate.now().plusDays(4));
            ordenRepository.save(orden);
            System.out.println("Estado de la orden actualizado a EN PROCESO.");
        } else if (normalizedResponse.equals("no")) {
            // El usuario desea cambiar la dirección de envío
            // Implementa la lógica para solicitar la nueva dirección y actualizarla
            // Agregar un nuevo mensaje al historial para solicitar la nueva dirección
            List<MessageGPTDto> messages = conversationCache.getIfPresent(sessionId);
            if (messages != null) {
                String promptNewAddress = "Por favor, proporciona la nueva dirección de envío.";
                messages.add(new MessageGPTDto("system", promptNewAddress));
                conversationCache.put(sessionId, messages);
            }
            // No cambiar el estado de la orden, permanece en "EN VALIDACION"
        } else {
            // Respuesta no reconocida, puedes optar por no hacer nada o manejarlo de otra manera
            System.out.println("Respuesta del usuario no reconocida para la validación de la dirección.");
        }
    }

    @PostMapping("/updateAddress")
    public ResponseEntity<String> updateAddress(@RequestBody UpdateAddressRequestDto request){
        try {
            // Validar que el orderId exista en la base de datos
            Orden order = ordenRepository.findById(Integer.valueOf(request.getOrderId()))
                    .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

            // Verificar que el estado actual es "EN VALIDACION"
            if (!"EN VALIDACION".equalsIgnoreCase(order.getEstadoordenIdestadoorden().getNombre())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La orden no está en estado 'EN VALIDACION'. No se puede actualizar la dirección.");
            }

            // Actualizar la dirección de envío
            order.setLugarEntrega(request.getNewAddress());

            // Guardar los cambios en la base de datos
            ordenRepository.save(order);

            // Actualizar el historial en el cache si es necesario
            // Agregar un mensaje de confirmación al historial
            List<MessageGPTDto> messages = conversationCache.getIfPresent(request.getSessionId());
            if (messages != null) {
                String confirmationMessage = "La dirección de envío ha sido actualizada exitosamente.";
                messages.add(new MessageGPTDto("assistant", confirmationMessage));
                conversationCache.put(request.getSessionId(), messages);
            }

            return ResponseEntity.ok("Dirección de envío actualizada exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }



    @GetMapping("/history")
    public @ResponseBody List<MessageGPTDto> getHistory(@RequestParam("sessionId") String sessionId){
        List<MessageGPTDto> messages = conversationCache.getIfPresent(sessionId);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    @DeleteMapping("/history")
    public ResponseEntity<String> deleteHistory(@RequestParam("sessionId") String sessionId){
        conversationCache.invalidate(sessionId);
        return ResponseEntity.ok("Historial eliminado exitosamente.");
    }
}


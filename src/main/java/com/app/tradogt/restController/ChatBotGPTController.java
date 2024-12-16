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
    private Cache<String, String> sessionStateCache; // Caché para estados de sesión

    @Autowired
    private OrdenRepository ordenRepository; // Repositorio de órdenes

    @Autowired
    private EstadoOrdenRepository estadoOrdenRepository;

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam("prompt") String prompt,
                                       @RequestParam("sessionId") String sessionId,
                                       @RequestParam("orderId") String orderId){
        try {
            // Validar que el orderId exista en la base de datos
            Orden order = ordenRepository.findById(Integer.valueOf(orderId))
                    .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

            // Obtener o crear el historial de conversación para la sesión
            List<MessageGPTDto> messages = conversationCache.getIfPresent(sessionId);
            if (messages == null) {
                messages = new ArrayList<>();
            }

            // Obtener el estado de la sesión
            String sessionState = sessionStateCache.getIfPresent(sessionId);
            if (sessionState == null) {
                sessionState = "IDLE";
                sessionStateCache.put(sessionId, sessionState);
            }

            // Si es una nueva conversación, agregar el mensaje de sistema con detalles de la orden
            if (messages.isEmpty()) {
                String fechaRecibidoStr = (order.getFechaRecibido() != null)
                        ? order.getFechaRecibido().toString()
                        : "No hay fecha de recibido";

                String systemMessage = String.format(
                        "Eres un asistente de compras inteligente que ayuda a los agentes a gestionar y verificar las órdenes de sus usuarios finales. " +
                                "Aquí están los detalles de la orden #%s:\n" +

                                "- Usuario: %s\n" +
                                "- Agente de compras: %s\n" +
                                "- Estado: %s\n" +
                                "- Fecha de Orden: %s\n" +
                                "- Costo Total: %s\n" +
                                "- Método de Pago: %s\n" +
                                "- Lugar de Entrega: %s\n" +
                                "- Fecha de Recibido: %s.\n\n" +

                                "El flujo de estados de la orden es el siguiente:\n" +
                                "1. **CREADO**: La orden fue registrada.\n" +
                                "2. **EN VALIDACIÓN**: Verificación de datos.\n" +
                                "3. **EN PROCESO**: Preparación y procesamiento de la orden.\n" +
                                "4. **ARRIBO AL PAÍS**: Llegada al país de destino.\n" +
                                "5. **EN ADUANAS**: Revisión aduanera.\n" +
                                "6. **EN RUTA**: En camino al destino final.\n" +
                                "7. **RECIBIDO**: La orden ha sido entregada.\n\n" +

                                "El agente puede:\n" +
                                "- Ver y editar las órdenes asignadas a sus usuarios finales.\n" +
                                "- Ver el estado de las órdenes accediendo al perfil del usuario o a la orden generada.\n" +
                                "- Bannear usuarios por mal comportamiento, datos falsos o pagos incompletos.\n\n" +

                                "El chatbot debe:\n" +
                                "- Conversar con el usuario final para verificar el número de orden generado.\n" +
                                "- Confirmar la dirección de delivery.\n" +
                                "- Indicar los costos adicionales que pueden afectar luego que la importación arribe al país.\n" +
                                "- Funcionará 24/7.",
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

                conversationCache.put(sessionId, messages);
            }


            // Agregar el mensaje del usuario
            messages.add(new MessageGPTDto("user", prompt));

            // Actualizar el historial en el cache
            conversationCache.put(sessionId, messages);

            // Procesar el mensaje según el estado de la sesión
            switch (sessionState) {
                case "VALIDATION_PENDING":
                    // El usuario está respondiendo a una solicitud de confirmación de dirección
                    handleValidationResponse(prompt, order, sessionId);
                    // Retornar una confirmación al frontend
                    return ResponseEntity.ok("Procesado la confirmación de la dirección de envío.");

                case "ADDRESS_PENDING":
                    // El usuario está proporcionando una nueva dirección de envío
                    handleAddressResponse(prompt, order, sessionId);
                    // Retornar una confirmación al frontend
                    return ResponseEntity.ok("Procesado la nueva dirección de envío.");

                default:
                    // Verificar si el usuario está solicitando validar su orden
                    if (isValidationRequest(prompt)) {
                        // Cambiar el estado de la sesión a VALIDATION_PENDING
                        sessionStateCache.put(sessionId, "VALIDATION_PENDING");

                        // Agregar mensaje de sistema solicitando confirmación
                        String validationMessage = "Por favor, confirma si estás conforme con la dirección de envío proporcionada. Si deseas realizar algún cambio, indícalo.";
                        messages.add(new MessageGPTDto("system", validationMessage));

                        // Actualizar el historial en el cache
                        conversationCache.put(sessionId, messages);

                        // Crear la solicitud con todo el historial
                        ChatGPTRequestDto requestDto = new ChatGPTRequestDto(model, messages);

                        try {
                            HttpEntity<ChatGPTRequestDto> entity = new HttpEntity<>(requestDto);
                            ResponseEntity<ChatGPTResponseDto> chatGptResponse = template.exchange(apiURL, HttpMethod.POST, entity, ChatGPTResponseDto.class);

                            if (chatGptResponse.getStatusCode() == HttpStatus.OK && chatGptResponse.getBody() != null) {
                                String assistantResponse = chatGptResponse.getBody().getChoices().get(0).getMessage().getContent();

                                // Agregar la respuesta del asistente al historial
                                messages.add(new MessageGPTDto("assistant", assistantResponse));

                                // Actualizar el historial en el cache
                                conversationCache.put(sessionId, messages);

                                return ResponseEntity.ok(assistantResponse);
                            } else {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la respuesta de OpenAI");
                            }
                        } catch (HttpClientErrorException.BadRequest e) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAsString());
                        } catch (HttpClientErrorException e) {
                            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
                        }
                    } else {
                        // No hay contexto especial, enviar el prompt al asistente
                        ChatGPTRequestDto requestDto = new ChatGPTRequestDto(model, messages);

                        try {
                            HttpEntity<ChatGPTRequestDto> entity = new HttpEntity<>(requestDto);
                            ResponseEntity<ChatGPTResponseDto> chatGptResponse = template.exchange(apiURL, HttpMethod.POST, entity, ChatGPTResponseDto.class);

                            if (chatGptResponse.getStatusCode() == HttpStatus.OK && chatGptResponse.getBody() != null) {
                                String assistantResponse = chatGptResponse.getBody().getChoices().get(0).getMessage().getContent();

                                // Agregar la respuesta del asistente al historial
                                messages.add(new MessageGPTDto("assistant", assistantResponse));

                                // Actualizar el historial en el cache
                                conversationCache.put(sessionId, messages);

                                return ResponseEntity.ok(assistantResponse);
                            } else {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la respuesta de OpenAI");
                            }
                        } catch (HttpClientErrorException.BadRequest e) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAsString());
                        } catch (HttpClientErrorException e) {
                            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
                        }
                    }
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    private boolean isValidationRequest(String prompt) {
        String normalizedPrompt = prompt.toLowerCase();
        return normalizedPrompt.contains("validar") || normalizedPrompt.contains("validación") || normalizedPrompt.contains("quiero validar");
    }

    private void handleValidationResponse(String userResponse, Orden orden, String sessionId) {
        // Normalizar la respuesta del usuario
        String normalizedResponse = userResponse.toLowerCase().replaceAll("[áàäâ]", "a")
                .replaceAll("[éèëê]", "e")
                .replaceAll("[íìïî]", "i")
                .replaceAll("[óòöô]", "o")
                .replaceAll("[úùüû]", "u")
                .trim();

        List<MessageGPTDto> messages = conversationCache.getIfPresent(sessionId);
        if (messages == null) {
            messages = new ArrayList<>();
        }

        if (normalizedResponse.startsWith("si")) {
            // El usuario está conforme, actualizar el estado de la orden
            orden.setEstadoordenIdestadoorden(
                    estadoOrdenRepository.findById(3)
                            .orElseThrow(() -> new IllegalArgumentException("Estado 'EN PROCESO' no encontrado"))
            );
            orden.setFechaEnProceso(LocalDate.now());
            orden.setFechaArribo(LocalDate.now().plusDays(1));
            orden.setFechaEnAduanas(LocalDate.now().plusDays(2));
            orden.setFechaEnRuta(LocalDate.now().plusDays(3));
            orden.setFechaRecibido(LocalDate.now().plusDays(4));
            ordenRepository.save(orden);
            System.out.println("Estado de la orden actualizado a EN PROCESO.");

            // Agregar mensaje de confirmación al historial
            String confirmationMessage = "Tu orden ha sido validada y ahora está en estado 'EN PROCESO'.";
            messages.add(new MessageGPTDto("assistant", confirmationMessage));
        } else if (normalizedResponse.startsWith("no")) {
            // El usuario desea cambiar la dirección de envío
            String promptNewAddress = "Por favor, proporciona la nueva dirección de envío.";
            messages.add(new MessageGPTDto("assistant", promptNewAddress)); // Cambiado a "assistant"
            System.out.println("Solicitando nueva dirección de envío.");

            // Cambiar el estado de la sesión a ADDRESS_PENDING
            sessionStateCache.put(sessionId, "ADDRESS_PENDING");
        } else if (normalizedResponse.startsWith("cancelar")) {
            // El usuario desea cancelar la validación de la orden
            orden.setEstadoordenIdestadoorden(
                    estadoOrdenRepository.findById(4)
                            .orElseThrow(() -> new IllegalArgumentException("Estado 'CANCELADA' no encontrado"))
            );
            ordenRepository.save(orden);
            System.out.println("Orden cancelada por el usuario.");

            // Agregar mensaje de confirmación de cancelación al historial
            String cancelMessage = "Has cancelado la validación de tu orden. La orden ahora está en estado 'CANCELADA'.";
            messages.add(new MessageGPTDto("assistant", cancelMessage));
        } else {
            // Respuesta no reconocida
            String unrecognizedResponse = "No entendí tu respuesta. Por favor, responde con 'Sí', 'No' o 'Cancelar'.";
            messages.add(new MessageGPTDto("assistant", unrecognizedResponse));
            System.out.println("Respuesta del usuario no reconocida para la validación de la dirección.");
        }

        // Actualizar el historial en el cache
        conversationCache.put(sessionId, messages);

        // Actualizar el estado de la sesión si es necesario
        if (normalizedResponse.startsWith("si") || normalizedResponse.startsWith("no") || normalizedResponse.startsWith("cancelar")) {
            if (!normalizedResponse.startsWith("no")) { // "No" cambia el estado a "ADDRESS_PENDING"
                // Reiniciar el estado de la sesión a "IDLE"
                sessionStateCache.put(sessionId, "IDLE");
            }
            // Si la respuesta es "No", el estado ya se ha cambiado a "ADDRESS_PENDING"
        } else {
            // Mantener el estado como "VALIDATION_PENDING" para solicitar una respuesta válida
            sessionStateCache.put(sessionId, "VALIDATION_PENDING");
        }
    }


    private void handleAddressResponse(String userResponse, Orden orden, String sessionId) {
        // Normalizar la respuesta del usuario
        String newAddress = userResponse.trim();

        List<MessageGPTDto> messages = conversationCache.getIfPresent(sessionId);
        if (messages == null) {
            messages = new ArrayList<>();
        }

        // Validar la longitud de la dirección
        if (newAddress.length() >= 10 && newAddress.length() <= 100) {
            // Dirección válida, actualizar en la orden
            orden.setLugarEntrega(newAddress);
            ordenRepository.save(orden);
            System.out.println("Dirección de envío actualizada exitosamente.");

            // Agregar mensaje de confirmación al historial
            String addressUpdatedMessage = "La dirección de envío ha sido actualizada exitosamente.";
            messages.add(new MessageGPTDto("assistant", addressUpdatedMessage));

            // Reiniciar el estado de la sesión a "VALIDATION_PENDING" para volver al flujo de decisión
            sessionStateCache.put(sessionId, "VALIDATION_PENDING");

            // Agregar mensaje de asistente solicitando confirmación nuevamente
            String validationMessage = "Por favor, confirma si estás conforme con la nueva dirección de envío proporcionada. Si deseas realizar algún cambio, indícalo.";
            messages.add(new MessageGPTDto("assistant", validationMessage));

        } else {
            // Dirección inválida, solicitar nuevamente
            String invalidAddressMessage = "La dirección debe tener entre 10 y 100 caracteres. Por favor, proporciona una nueva dirección de envío.";
            messages.add(new MessageGPTDto("assistant", invalidAddressMessage));
            System.out.println("Dirección inválida proporcionada. Solicitando nuevamente.");

            // Mantener el estado como "ADDRESS_PENDING"
            sessionStateCache.put(sessionId, "ADDRESS_PENDING");
        }

        // Actualizar el historial en el cache
        conversationCache.put(sessionId, messages);
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

            // Validar la longitud de la nueva dirección
            String newAddress = request.getNewAddress().trim();
            if (newAddress.length() < 10 || newAddress.length() > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La dirección debe tener entre 10 y 100 caracteres.");
            }

            // Actualizar la dirección de envío
            order.setLugarEntrega(newAddress);

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

            // Después de actualizar la dirección, volver al flujo de validación
            // Agregar mensaje de confirmación y solicitar confirmación nuevamente
            if (messages != null) {
                String validationMessage = "Por favor, confirma si estás conforme con la nueva dirección de envío proporcionada. Si deseas realizar algún cambio, indícalo.";
                messages.add(new MessageGPTDto("assistant", validationMessage));
                conversationCache.put(request.getSessionId(), messages);
            }

            // Cambiar el estado de la sesión a "VALIDATION_PENDING"
            sessionStateCache.put(request.getSessionId(), "VALIDATION_PENDING");

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
        sessionStateCache.invalidate(sessionId);
        return ResponseEntity.ok("Historial eliminado exitosamente.");
    }
}


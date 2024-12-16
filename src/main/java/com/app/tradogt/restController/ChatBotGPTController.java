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
                        "Eres **AlexIA**, un asistente de compras inteligente que ayuda a los agentes a gestionar y verificar las órdenes de sus usuarios finales. " +
                                "Aquí están los detalles de la orden #%s:\n\n" +

                                "- **Usuario**: %s\n" +
                                "- **Agente de compras**: %s\n" +
                                "- **Estado**: %s\n" +
                                "- **Fecha de Orden**: %s\n" +
                                "- **Costo Total**: %s\n" +
                                "- **Método de Pago**: %s\n" +
                                "- **Lugar de Entrega**: %s\n" +
                                "- **Fecha de Recibido**: %s.\n\n" +

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
                                "- Brindar el enlace al libro de reclamaciones siempre que el usuario lo solicite, el link es https://forms.gle/9EKiu2iSA1kN83wFA.\n" +
                                "- Funcionará 24/7.\n\n" +

                                "**Nota Importante**: La validación de la dirección solo puede realizarse cuando el estado de la orden es **EN VALIDACION**. Si la orden no está en este estado, no se puede proceder con la validación. ❗\n\n" +

                                "**AlexIA** responde utilizando emojis para una conversación más amigable. 😊👍",

                        order.getCodigo(),
                        order.getUsuarioIdusuario().getNombre(),
                        order.getAgentcompraIdusuario().getNombre(),
                        order.getEstadoordenIdestadoorden().getNombre(),
                        order.getFechaCreacion().toString(),
                        order.getCostoTotal().toString(),
                        order.getPagoIdpago().getMetodo().toString(),
                        order.getLugarEntrega(),
                        fechaRecibidoStr
                );
                messages.add(new MessageGPTDto("system", systemMessage));

                conversationCache.put(sessionId, messages);
            }


            // Agregar el mensaje del usuario
            messages.add(new MessageGPTDto("user", prompt));
            conversationCache.put(sessionId, messages);

            switch (sessionState) {
                case "VALIDATION_PENDING":
                    // El usuario está respondiendo a una solicitud de confirmación de dirección
                    handleValidationResponse(prompt, order, sessionId);
                    return ResponseEntity.ok("Procesado la confirmación de la dirección de envío.");

                case "ADDRESS_PENDING":
                    // El usuario está proporcionando una nueva dirección de envío
                    handleAddressResponse(prompt, order, sessionId);
                    return ResponseEntity.ok("Procesado la nueva dirección de envío.");

                default:
                    // Verificar si el usuario quiere validar la orden (y si la orden está en EN VALIDACION)
                    if (isValidationRequest(prompt, order)) {
                        // Solo entrar a VALIDATION_PENDING si la orden sigue en EN VALIDACION
                        if ("EN VALIDACION".equalsIgnoreCase(order.getEstadoordenIdestadoorden().getNombre())) {
                            sessionStateCache.put(sessionId, "VALIDATION_PENDING");
                        } else {
                            sessionStateCache.put(sessionId, "IDLE");
                        }

                        // Agregar mensaje de sistema solicitando confirmación
                        String validationMessage = "Procede a enviarle al usuario un mensaje donde debe confirmar si su dirección  de entrega es la correcta, al usuario se le dará a escoger 3 opciones, sí, no y cancelar, incluye siempre en tu mensaje este fragmento 'solicitud de validación'.";
                        messages.add(new MessageGPTDto("system", validationMessage));
                        conversationCache.put(sessionId, messages);

                        // Crear la solicitud con todo el historial
                        ChatGPTRequestDto requestDto = new ChatGPTRequestDto(model, messages);

                        try {
                            HttpEntity<ChatGPTRequestDto> entity = new HttpEntity<>(requestDto);
                            ResponseEntity<ChatGPTResponseDto> chatGptResponse = template.exchange(apiURL, HttpMethod.POST, entity, ChatGPTResponseDto.class);

                            if (chatGptResponse.getStatusCode() == HttpStatus.OK && chatGptResponse.getBody() != null) {
                                String assistantResponse = chatGptResponse.getBody().getChoices().get(0).getMessage().getContent();

                                messages.add(new MessageGPTDto("assistant", assistantResponse));
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

                                messages.add(new MessageGPTDto("assistant", assistantResponse));
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

    // Nueva versión: Verifica también si la orden está en EN VALIDACION
    private boolean isValidationRequest(String prompt, Orden order) {
        String normalizedPrompt = prompt.toLowerCase();
        boolean wantsValidation = normalizedPrompt.contains("validar") || normalizedPrompt.contains("validación") || normalizedPrompt.contains("quiero validar");
        return wantsValidation && "EN VALIDACION".equalsIgnoreCase(order.getEstadoordenIdestadoorden().getNombre());
    }

    private void handleValidationResponse(String userResponse, Orden orden, String sessionId) {
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
            // Usuario conforme -> Actualizar orden a EN PROCESO
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

            String confirmationMessage = "Tu orden ha sido validada y ahora está en estado 'EN PROCESO'.";
            messages.add(new MessageGPTDto("assistant", confirmationMessage));
            // Estado de sesión a IDLE (validación completada)
            sessionStateCache.put(sessionId, "IDLE");

        } else if (normalizedResponse.startsWith("no")) {
            // Usuario no conforme -> Solicitar nueva dirección
            String promptNewAddress = "Por favor, proporciona la nueva dirección de envío.";
            messages.add(new MessageGPTDto("assistant", promptNewAddress));
            sessionStateCache.put(sessionId, "ADDRESS_PENDING");

        } else if (normalizedResponse.startsWith("cancelar")) {
            // Usuario cancela -> Actualizar orden a CANCELADA
            orden.setEstadoordenIdestadoorden(
                    estadoOrdenRepository.findById(4)
                            .orElseThrow(() -> new IllegalArgumentException("Estado 'CANCELADA' no encontrado"))
            );
            ordenRepository.save(orden);

            String cancelMessage = "Has cancelado la validación de tu orden.";
            messages.add(new MessageGPTDto("assistant", cancelMessage));
            // Estado a IDLE ya que la validación se canceló
            sessionStateCache.put(sessionId, "IDLE");

        } else {
            // Respuesta no reconocida
            String unrecognizedResponse = "No entendí tu respuesta. Por favor, responde con 'Sí', 'No' o 'Cancelar'.";
            messages.add(new MessageGPTDto("assistant", unrecognizedResponse));

            // Solo mantener VALIDATION_PENDING si la orden sigue en EN VALIDACION
            if ("EN VALIDACION".equalsIgnoreCase(orden.getEstadoordenIdestadoorden().getNombre())) {
                sessionStateCache.put(sessionId, "VALIDATION_PENDING");
            } else {
                sessionStateCache.put(sessionId, "IDLE");
            }
        }

        conversationCache.put(sessionId, messages);
    }

    private void handleAddressResponse(String userResponse, Orden orden, String sessionId) {
        String newAddress = userResponse.trim();
        List<MessageGPTDto> messages = conversationCache.getIfPresent(sessionId);
        if (messages == null) {
            messages = new ArrayList<>();
        }

        if (newAddress.length() >= 10 && newAddress.length() <= 100) {
            // Dirección válida
            orden.setLugarEntrega(newAddress);
            ordenRepository.save(orden);

            String addressUpdatedMessage = "La dirección de envío ha sido actualizada exitosamente.";
            messages.add(new MessageGPTDto("assistant", addressUpdatedMessage));

            // Verificar si la orden sigue en EN VALIDACION antes de volver a VALIDATION_PENDING
            if ("EN VALIDACION".equalsIgnoreCase(orden.getEstadoordenIdestadoorden().getNombre())) {
                sessionStateCache.put(sessionId, "VALIDATION_PENDING");
                String validationMessage = "Por favor, confirma si estás conforme con la nueva dirección de envío proporcionada. Si deseas realizar algún cambio, indícalo.";
                messages.add(new MessageGPTDto("assistant", validationMessage));
            } else {
                // Si la orden ya no está en EN VALIDACION, estado a IDLE
                sessionStateCache.put(sessionId, "IDLE");
            }

        } else {
            // Dirección inválida
            String invalidAddressMessage = "La dirección debe tener entre 10 y 100 caracteres. Por favor, proporciona una nueva dirección de envío.";
            messages.add(new MessageGPTDto("assistant", invalidAddressMessage));
            sessionStateCache.put(sessionId, "ADDRESS_PENDING");
        }

        conversationCache.put(sessionId, messages);
    }

    @PostMapping("/updateAddress")
    public ResponseEntity<String> updateAddress(@RequestBody UpdateAddressRequestDto request){
        try {
            Orden order = ordenRepository.findById(Integer.valueOf(request.getOrderId()))
                    .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

            if (!"EN VALIDACION".equalsIgnoreCase(order.getEstadoordenIdestadoorden().getNombre())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La orden no está en estado 'EN VALIDACION'. No se puede actualizar la dirección.");
            }

            String newAddress = request.getNewAddress().trim();
            if (newAddress.length() < 10 || newAddress.length() > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La dirección debe tener entre 10 y 100 caracteres.");
            }

            order.setLugarEntrega(newAddress);
            ordenRepository.save(order);

            List<MessageGPTDto> messages = conversationCache.getIfPresent(request.getSessionId());
            if (messages == null) {
                messages = new ArrayList<>();
            }

            String confirmationMessage = "La dirección de envío ha sido actualizada exitosamente.";
            messages.add(new MessageGPTDto("assistant", confirmationMessage));

            // Verificar si la orden sigue en EN VALIDACION antes de poner VALIDATION_PENDING
            if ("EN VALIDACION".equalsIgnoreCase(order.getEstadoordenIdestadoorden().getNombre())) {
                String validationMessage = "Por favor, confirma si estás conforme con la nueva dirección de envío proporcionada. Si deseas realizar algún cambio, indícalo.";
                messages.add(new MessageGPTDto("assistant", validationMessage));
                sessionStateCache.put(request.getSessionId(), "VALIDATION_PENDING");
            } else {
                // Si la orden ya no está en EN VALIDACION, estado a IDLE
                sessionStateCache.put(request.getSessionId(), "IDLE");
            }

            conversationCache.put(request.getSessionId(), messages);

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

package com.app.tradogt.restController;

import com.app.tradogt.dto.ChatGPTRequestDto;
import com.app.tradogt.dto.ChatGPTResponseDto;
import com.app.tradogt.dto.MessageGPTDto;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.repository.OrdenRepository;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
                    order.getFechaRecibido().toString()
            );
            messages.add(new MessageGPTDto("system", systemMessage));
        }

        // Agregar el mensaje del usuario
        messages.add(new MessageGPTDto("user", prompt));

        // Crear la solicitud con todo el historial
        ChatGPTRequestDto request = new ChatGPTRequestDto(model, messages);

        try {
            // Realizar la llamada a la API de OpenAI
            ChatGPTResponseDto chatGptResponse = template.postForObject(apiURL, request, ChatGPTResponseDto.class);

            // Obtener la respuesta del modelo
            String response = chatGptResponse.getChoices().get(0).getMessage().getContent();

            // Agregar la respuesta del asistente al historial
            messages.add(new MessageGPTDto("assistant", response));

            // Actualizar el historial en el cache
            conversationCache.put(sessionId, messages);

            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException e) {
            // Manejar errores específicos de la API
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
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
}


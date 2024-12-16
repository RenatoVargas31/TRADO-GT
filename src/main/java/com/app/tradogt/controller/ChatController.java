package com.app.tradogt.controller;

import com.app.tradogt.entity.ChatMessage;
import com.app.tradogt.entity.Message;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Usuario;
import com.app.tradogt.repository.MessageRepository;
import com.app.tradogt.repository.OrdenRepository;
import com.app.tradogt.repository.UsuarioRepository;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * Controller class for handling chat-related functionality.
 */
@Controller
public class ChatController {

    final OrdenRepository ordenRepository;
    final UsuarioRepository usuarioRepository;
    final MessageRepository messageRepository;

    public ChatController(OrdenRepository ordenRepository, UsuarioRepository usuarioRepository, MessageRepository messageRepository) {
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
        this.messageRepository = messageRepository;
    }


    @MessageMapping("/chat.register/{orderId}")
    @SendTo("/topic/public/{orderId}")
    public ChatMessage register(@Payload ChatMessage chatMessage,@DestinationVariable String orderId, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("orderId", orderId);
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }


    @MessageMapping("/chat.send/{orderId}")
    @SendTo("/topic/public/{orderId}")
    public ChatMessage sendMessage(@DestinationVariable String orderId,@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.save/{orderId}")
    public void saveMessage(@DestinationVariable String orderId,@Payload ChatMessage chatMessage) {
        Message message = new Message();
        Optional<Usuario> usuario = usuarioRepository.findById(Integer.parseInt(chatMessage.getIdSender()));
        Optional<Orden> orden = ordenRepository.findById(Integer.parseInt(chatMessage.getIdOrder()));

        message.setMensaje(chatMessage.getContent());
        message.setSender(usuario.get());
        message.setOrden(orden.get());
        message.setFechaEnvio(LocalDateTime.now());

        messageRepository.save(message);

    }
}

package com.app.tradogt.controller;

import com.app.tradogt.entity.ChatMessage;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Controller class for handling chat-related functionality.
 */
@Controller
public class ChatController {


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
}

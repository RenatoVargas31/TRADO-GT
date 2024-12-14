package com.app.tradogt.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequestDto {

    private String model;
    private List<MessageGPTDto> messages;

    /**
     * Constructor que acepta una lista completa de mensajes
     * @param model El modelo de GPT a utilizar
     * @param messages Lista de mensajes en la conversación
     */
    public ChatGPTRequestDto(String model, List<MessageGPTDto> messages) {
        this.model = model;
        this.messages = messages;
    }
}
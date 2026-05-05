package br.com.fiap.wtccrm.chat.controller;

import br.com.fiap.wtccrm.chat.dto.MessageDTO;
import br.com.fiap.wtccrm.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * STOMP: recebe em {@code /app/chat.send} (prefixo configurado em
 * {@link br.com.fiap.wtccrm.config.WebSocketConfig}),
 * persiste via {@link ChatService}. O broadcast para {@code /topic/conversation/{id}} é feito em {@link ChatService}.
 */
@Controller
public class ChatWebSocketController {

    private final ChatService chatService;

    public ChatWebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Cliente STOMP envia para {@code /app/chat.send}; mensagem é persistida e replicada no tópico da conversa.
     */
    @MessageMapping("/chat.send")
    public void handleMessage(@Payload MessageDTO payload) {
        chatService.sendMessage(payload.conversationId(), payload);
    }
}

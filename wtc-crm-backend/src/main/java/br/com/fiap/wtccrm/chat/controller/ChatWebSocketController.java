package br.com.fiap.wtccrm.chat.controller;

import br.com.fiap.wtccrm.chat.dto.MessageDTO;
import br.com.fiap.wtccrm.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void handleMessage(@Payload MessageDTO payload) {
        MessageDTO saved = chatService.sendMessage(payload.conversationId(), payload);
        messagingTemplate.convertAndSend("/topic/conversation/" + saved.conversationId(), saved);
    }
}

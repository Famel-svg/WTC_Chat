package br.com.fiap.wtccrm.chat.controller;

import br.com.fiap.wtccrm.chat.dto.ConversationDTO;
import br.com.fiap.wtccrm.chat.dto.MessageDTO;
import br.com.fiap.wtccrm.chat.service.ChatService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/conversations")
    public List<ConversationDTO> conversations() {
        return chatService.listConversations();
    }

    @GetMapping("/conversations/{id}/messages")
    public List<MessageDTO> messages(@PathVariable String id) {
        return chatService.listMessages(id);
    }

    @PostMapping("/conversations")
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationDTO createConversation(@RequestBody ConversationDTO dto) {
        return chatService.createConversation(dto);
    }

    @PostMapping("/conversations/{id}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO sendMessage(@PathVariable String id, @RequestBody MessageDTO dto) {
        return chatService.sendMessage(id, dto);
    }

    @PutMapping("/messages/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable String id, @RequestBody String status) {
        chatService.updateStatus(id, status);
    }
}

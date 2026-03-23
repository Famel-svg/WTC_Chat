package br.com.fiap.wtccrm.chat.service;

import br.com.fiap.wtccrm.chat.dto.ConversationDTO;
import br.com.fiap.wtccrm.chat.dto.MessageDTO;
import br.com.fiap.wtccrm.exception.BusinessException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Persistência em memória de conversas e mensagens (mapas). Em produção, trocar por Firestore:
 * coleção {@code conversations} e subcoleção {@code messages}.
 */
@Service
public class ChatService {

    private final Map<String, ConversationDTO> conversations = new ConcurrentHashMap<>();
    private final Map<String, List<MessageDTO>> messagesByConversation = new ConcurrentHashMap<>();

    public List<ConversationDTO> listConversations() {
        return conversations.values().stream()
                .sorted(Comparator.comparing(ConversationDTO::lastMessageAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();
    }

    public List<MessageDTO> listMessages(String conversationId) {
        return messagesByConversation.getOrDefault(conversationId, List.of());
    }

    public ConversationDTO createConversation(ConversationDTO request) {
        ConversationDTO dto = new ConversationDTO(
                UUID.randomUUID().toString(),
                request.type() == null ? "ONE_TO_ONE" : request.type(),
                request.name(),
                request.participantIds(),
                null,
                Instant.now());
        conversations.put(dto.id(), dto);
        messagesByConversation.put(dto.id(), new ArrayList<>());
        return dto;
    }

    public MessageDTO sendMessage(String conversationId, MessageDTO request) {
        ConversationDTO conv = conversations.get(conversationId);
        if (conv == null) {
            throw new BusinessException("Conversation not found");
        }
        MessageDTO dto = new MessageDTO(
                UUID.randomUUID().toString(),
                conversationId,
                request.senderId(),
                request.senderName(),
                request.content(),
                request.mediaUrl(),
                request.type() == null ? "TEXT" : request.type(),
                "SENT",
                Instant.now());
        messagesByConversation.computeIfAbsent(conversationId, k -> new ArrayList<>()).add(dto);
        conversations.put(conversationId, new ConversationDTO(
                conv.id(),
                conv.type(),
                conv.name(),
                conv.participantIds(),
                dto.content(),
                dto.sentAt()));
        return dto;
    }

    public void updateStatus(String messageId, String status) {
        for (Map.Entry<String, List<MessageDTO>> e : messagesByConversation.entrySet()) {
            List<MessageDTO> updated = e.getValue().stream().map(m -> {
                if (!m.id().equals(messageId)) {
                    return m;
                }
                return new MessageDTO(m.id(), m.conversationId(), m.senderId(), m.senderName(), m.content(), m.mediaUrl(), m.type(), status, m.sentAt());
            }).toList();
            messagesByConversation.put(e.getKey(), new ArrayList<>(updated));
        }
    }
}

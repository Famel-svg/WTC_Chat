package br.com.fiap.wtccrm.chat.service;

import br.com.fiap.wtccrm.chat.dto.ConversationDTO;
import br.com.fiap.wtccrm.chat.dto.MessageDTO;
import br.com.fiap.wtccrm.exception.BusinessException;
import br.com.fiap.wtccrm.persistence.document.ConversationDocument;
import br.com.fiap.wtccrm.persistence.document.MessageDocument;
import br.com.fiap.wtccrm.notifications.service.PushNotificationService;
import br.com.fiap.wtccrm.persistence.document.UserDocument;
import br.com.fiap.wtccrm.persistence.repository.ConversationRepository;
import br.com.fiap.wtccrm.persistence.repository.MessageRepository;
import br.com.fiap.wtccrm.persistence.repository.UserRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Conversas e mensagens persistidas no MongoDB ({@code conversations}, {@code messages}).
 */
@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final PushNotificationService pushNotificationService;
    private final UserRepository userRepository;

    public ChatService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            SimpMessagingTemplate messagingTemplate,
            PushNotificationService pushNotificationService,
            UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.pushNotificationService = pushNotificationService;
        this.userRepository = userRepository;
    }

    public List<ConversationDTO> listConversations() {
        return conversationRepository.findAll().stream()
                .map(this::convToDto)
                .sorted(Comparator.comparing(ConversationDTO::lastMessageAt, Comparator.nullsLast(Comparator.naturalOrder()))
                        .reversed())
                .toList();
    }

    public List<MessageDTO> listMessages(String conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId).stream()
                .map(this::msgToDto)
                .toList();
    }

    public ConversationDTO createConversation(ConversationDTO request) {
        ConversationDocument doc = new ConversationDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setType(request.type() == null ? "ONE_TO_ONE" : request.type());
        doc.setName(request.name());
        doc.setParticipantIds(request.participantIds());
        doc.setLastMessageContent(null);
        doc.setLastMessageAt(Instant.now());
        conversationRepository.save(doc);
        return convToDto(doc);
    }

    public MessageDTO sendMessage(String conversationId, MessageDTO request) {
        ConversationDocument conv = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new BusinessException("Conversation not found"));
        MessageDocument msg = new MessageDocument();
        msg.setId(UUID.randomUUID().toString());
        msg.setConversationId(conversationId);
        msg.setSenderId(request.senderId());
        msg.setSenderName(request.senderName());
        msg.setContent(request.content());
        msg.setMediaUrl(request.mediaUrl());
        msg.setType(request.type() == null ? "TEXT" : request.type());
        msg.setStatus("SENT");
        msg.setSentAt(Instant.now());
        messageRepository.save(msg);

        conv.setLastMessageContent(msg.getContent());
        conv.setLastMessageAt(msg.getSentAt());
        conversationRepository.save(conv);

        MessageDTO dto = msgToDto(msg);
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, dto);
        notifyParticipantsPush(dto, conv);
        return dto;
    }

    private void notifyParticipantsPush(MessageDTO saved, ConversationDocument conv) {
        if (conv.getParticipantIds() == null) {
            return;
        }
        String preview =
                saved.content() != null && saved.content().length() > 120
                        ? saved.content().substring(0, 117) + "..."
                        : saved.content() != null ? saved.content() : "";
        Map<String, String> data = new HashMap<>();
        data.put("type", "CHAT_MESSAGE");
        data.put("conversationId", saved.conversationId());
        data.put("messageId", saved.id());
        for (String userId : conv.getParticipantIds()) {
            if (userId.equals(saved.senderId())) {
                continue;
            }
            userRepository
                    .findById(userId)
                    .map(UserDocument::getFcmToken)
                    .filter(t -> t != null && !t.isBlank())
                    .ifPresent(token -> pushNotificationService.sendPush(
                            token,
                            saved.senderName() != null ? saved.senderName() : "Nova mensagem",
                            preview,
                            data));
        }
    }

    public void updateStatus(String messageId, String status) {
        MessageDocument msg = messageRepository
                .findById(messageId)
                .orElseThrow(() -> new BusinessException("Message not found"));
        msg.setStatus(status);
        messageRepository.save(msg);
    }

    private ConversationDTO convToDto(ConversationDocument c) {
        return new ConversationDTO(
                c.getId(),
                c.getType(),
                c.getName(),
                c.getParticipantIds(),
                c.getLastMessageContent(),
                c.getLastMessageAt());
    }

    private MessageDTO msgToDto(MessageDocument m) {
        return new MessageDTO(
                m.getId(),
                m.getConversationId(),
                m.getSenderId(),
                m.getSenderName(),
                m.getContent(),
                m.getMediaUrl(),
                m.getType(),
                m.getStatus(),
                m.getSentAt());
    }
}

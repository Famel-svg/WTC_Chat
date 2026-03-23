package br.com.fiap.wtccrm.chat.dto;

import java.time.Instant;
import java.util.List;

public record ConversationDTO(
        String id,
        String type,
        String name,
        List<String> participantIds,
        String lastMessageContent,
        Instant lastMessageAt) {
}

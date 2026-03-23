package br.com.fiap.wtccrm.chat.dto;

import java.time.Instant;

public record MessageDTO(
        String id,
        String conversationId,
        String senderId,
        String senderName,
        String content,
        String mediaUrl,
        String type,
        String status,
        Instant sentAt) {
}

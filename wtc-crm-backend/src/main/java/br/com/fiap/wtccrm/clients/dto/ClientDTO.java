package br.com.fiap.wtccrm.clients.dto;

import java.time.Instant;

public record ClientDTO(
        String id,
        String name,
        String email,
        String phone,
        String category,
        int score,
        String status,
        String segmentId,
        String avatarUrl,
        Instant createdAt,
        Instant updatedAt) {
}

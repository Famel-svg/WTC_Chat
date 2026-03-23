package br.com.fiap.wtccrm.tasks.dto;

import java.time.Instant;

public record TaskDTO(
        String id,
        String title,
        String description,
        String clientId,
        String operatorId,
        String status,
        int priority,
        Instant dueDate,
        Instant createdAt,
        Instant updatedAt) {
}

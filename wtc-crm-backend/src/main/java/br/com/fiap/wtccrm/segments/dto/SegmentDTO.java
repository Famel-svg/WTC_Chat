package br.com.fiap.wtccrm.segments.dto;

import java.time.Instant;

public record SegmentDTO(
        String id,
        String name,
        String description,
        String color,
        int clientCount,
        Instant createdAt) {
}

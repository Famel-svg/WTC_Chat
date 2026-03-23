package br.com.fiap.wtccrm.exception.dto;

import java.time.Instant;

public record ErrorResponseDTO(
        String message,
        String path,
        int status,
        Instant timestamp) {
}

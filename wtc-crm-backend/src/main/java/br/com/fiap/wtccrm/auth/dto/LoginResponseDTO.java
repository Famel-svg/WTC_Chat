package br.com.fiap.wtccrm.auth.dto;

public record LoginResponseDTO(
        String token,
        String userId,
        String role,
        String name) {
}

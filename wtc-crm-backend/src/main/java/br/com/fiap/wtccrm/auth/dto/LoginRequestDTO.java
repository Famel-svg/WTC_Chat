package br.com.fiap.wtccrm.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String email,
        @NotBlank String password) {
}

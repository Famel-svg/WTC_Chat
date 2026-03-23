package br.com.fiap.wtchat.api.dto

/** Corpo JSON de `POST /api/auth/login` (campos `email` e `password`). */
data class LoginRequestDTO(
    val email: String,
    val password: String
)


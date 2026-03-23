package br.com.fiap.wtchat.api.dto

/** Resposta do login: token JWT e metadados do usuário. */
data class LoginResponseDTO(
    val token: String,
    val userId: String,
    val role: String,
    val name: String
)


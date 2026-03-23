package br.com.fiap.wtchat.api.dto

data class LoginResponseDTO(
    val token: String,
    val userId: String,
    val role: String,
    val name: String
)


package br.com.fiap.wtchat.api.dto

data class ClientDTO(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val category: String? = null,
    val score: Int = 0,
    val status: String? = null,
    val segmentId: String? = null,
    val avatarUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

package br.com.fiap.wtchat.api.dto

data class SegmentDTO(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val color: String? = null,
    val clientCount: Int = 0,
    val createdAt: String? = null
)

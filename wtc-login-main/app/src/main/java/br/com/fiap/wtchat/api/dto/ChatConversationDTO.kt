package br.com.fiap.wtchat.api.dto

data class ChatConversationDTO(
    val id: String,
    val type: String?,
    val name: String?,
    val participantIds: List<String>?,
    val lastMessageContent: String?,
    val lastMessageAt: String?
)


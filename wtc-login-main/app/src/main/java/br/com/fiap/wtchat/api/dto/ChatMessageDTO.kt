package br.com.fiap.wtchat.api.dto

data class ChatMessageDTO(
    val id: String? = null,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val mediaUrl: String? = null,
    val type: String? = "TEXT",
    val status: String? = null,
    val sentAt: String? = null
)


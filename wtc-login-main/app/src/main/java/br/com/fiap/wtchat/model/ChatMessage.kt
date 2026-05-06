package br.com.fiap.wtchat.model

data class ChatMessage(
    val id: String?,
    val text: String,
    val isUser: Boolean,
    val timestamp: String
)

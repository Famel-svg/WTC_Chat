package br.com.fiap.wtchat.model

data class ChatUiModel(
    val id: String?,
    val messageText: String,
    val isSentByUser: Boolean,
    val formattedTime: String,
    val status: String? = null,
    val mediaUrl: String? = null
)

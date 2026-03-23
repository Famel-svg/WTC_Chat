package br.com.fiap.wtchat.viewmodel

import android.util.Log
import androidx.compose.runtime.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.wtchat.api.RetrofitInstance
import br.com.fiap.wtchat.api.dto.ChatMessageDTO
import br.com.fiap.wtchat.auth.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: String
)


class ChatViewModel : ViewModel() {

    private var conversationId: String? = null

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: SnapshotStateList<ChatMessage> = _messages

    fun connect(conversationId: String) {
        this.conversationId = conversationId
        _messages.clear()

        val session = SessionManager.session ?: return

        viewModelScope.launch {
            try {
                val auth = "Bearer ${session.token}"
                val apiMessages = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getMessages(auth, conversationId)
                }
                _messages.addAll(apiMessages.map { it.toUiMessage(currentUserId = session.userId) })
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Falha ao carregar mensagens", e)
            }
        }
    }

    fun sendMessage(text: String) {
        val cid = conversationId ?: return
        if (text.isBlank()) return

        val session = SessionManager.session ?: return

        viewModelScope.launch {
            try {
                val auth = "Bearer ${session.token}"
                val request = ChatMessageDTO(
                    conversationId = cid,
                    senderId = session.userId,
                    senderName = session.name,
                    content = text,
                    type = "TEXT",
                    mediaUrl = null,
                    status = null,
                    sentAt = null
                )

                val saved = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.sendMessage(auth, cid, request)
                }
                _messages.add(saved.toUiMessage(currentUserId = session.userId))
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Falha ao enviar mensagem", e)
            }
        }
    }


    private fun ChatMessageDTO.toUiMessage(currentUserId: String): ChatMessage {
        val isUser = senderId == currentUserId
        return ChatMessage(
            text = content,
            isUser = isUser,
            timestamp = formatSentAt(sentAt)
        )
    }

    private fun formatSentAt(sentAt: String?): String {
        if (sentAt.isNullOrBlank()) return "--:--"
        return try {
            val instant = Instant.parse(sentAt)
            DateTimeFormatter.ofPattern("HH:mm")
                .withZone(ZoneId.systemDefault())
                .format(instant)
        } catch (e: Exception) {
            "--:--"
        }
    }
}
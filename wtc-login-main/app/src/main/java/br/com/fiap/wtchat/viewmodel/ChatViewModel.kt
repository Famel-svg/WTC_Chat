/**
 * Estado e lógica da tela de chat: carrega mensagens via GET e envia via POST no backend.
 * [connect] deve ser chamado com o `conversationId` vindo da navegação.
 */
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


/** Item de UI: texto, se é do usuário logado e horário formatado. */
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: String
)


class ChatViewModel : ViewModel() {

    private var conversationId: String? = null

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: SnapshotStateList<ChatMessage> = _messages

    /** Carrega histórico da conversa e prepara envio para este [conversationId]. */
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

    /** Envia texto ao backend; o servidor devolve a mensagem persistida com id e timestamps. */
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


    /** Converte DTO da API para bolha de UI (lado usuário vs outro). */
    private fun ChatMessageDTO.toUiMessage(currentUserId: String): ChatMessage {
        val isUser = senderId == currentUserId
        return ChatMessage(
            text = content,
            isUser = isUser,
            timestamp = formatSentAt(sentAt)
        )
    }

    /** ISO-8601 do backend → hora local HH:mm. */
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
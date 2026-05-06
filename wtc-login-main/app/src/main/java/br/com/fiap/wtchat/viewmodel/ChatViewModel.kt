package br.com.fiap.wtchat.viewmodel

import android.util.Log
import androidx.compose.runtime.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.wtchat.api.RetrofitInstance
import br.com.fiap.wtchat.api.dto.ChatMessageDTO
import br.com.fiap.wtchat.auth.SessionManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import br.com.fiap.wtchat.model.ChatMessage

class ChatViewModel : ViewModel() {

    private var conversationId: String? = null
    private val seenIds = mutableSetOf<String>()
    private var chatDatabaseRef: DatabaseReference? = null
    private var chatEventListener: ChildEventListener? = null

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: SnapshotStateList<ChatMessage> = _messages

    fun connect(convId: String) {
        conversationId = convId
        _messages.clear()
        seenIds.clear()
        disconnectFirebaseChatListener()

        val session = SessionManager.session ?: return

        viewModelScope.launch {
            try {
                // Carregar histórico de mensagens via REST (mantido)
                val auth = "Bearer ${session.token}"
                val apiMessages = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getMessages(auth, convId)
                }
                apiMessages.forEach { dto ->
                    dto.id?.let { seenIds.add(it) }
                    _messages.add(dto.toUiMessage(session.userId))
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Falha ao carregar mensagens via REST", e)
            }
        }

        // Conectar ao Firebase Realtime Database para mensagens em tempo real
        connectFirebaseChatListener(convId, session.userId)
    }

    private fun connectFirebaseChatListener(convId: String, currentUserId: String) {
        chatDatabaseRef = FirebaseDatabase.getInstance().getReference("chats").child(convId)

        chatEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val messageDTO = snapshot.getValue(ChatMessageDTO::class.java)
                messageDTO?.let { dto ->
                    val mid = dto.id ?: snapshot.key // Usar snapshot.key se dto.id for nulo
                    if (mid != null && mid !in seenIds) {
                        seenIds.add(mid)
                        _messages.add(dto.toUiMessage(currentUserId))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Lógica para lidar com mensagens alteradas (ex: status de lido)
                val changedMessageDTO = snapshot.getValue(ChatMessageDTO::class.java)
                changedMessageDTO?.let { dto ->
                    val mid = dto.id ?: snapshot.key
                    if (mid != null) {
                        val index = _messages.indexOfFirst { it.id == mid }
                        if (index != -1) {
                            _messages[index] = dto.toUiMessage(currentUserId)
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Lógica para lidar com mensagens removidas
                val removedMessageId = snapshot.key
                removedMessageId?.let { mid ->
                    _messages.removeIf { it.id == mid }
                    seenIds.remove(mid)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não é comum para chat, mas pode ser implementado se a ordem for importante
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatViewModel", "Firebase Chat Listener Cancelled: ${error.message}")
            }
        }
        chatDatabaseRef?.addChildEventListener(chatEventListener!!)
    }

    private fun disconnectFirebaseChatListener() {
        chatDatabaseRef?.removeEventListener(chatEventListener!!)
        chatEventListener = null
        chatDatabaseRef = null
    }

    fun sendMessage(text: String) {
        val cid = conversationId ?: return
        if (text.isBlank()) return

        val session = SessionManager.session ?: return

        viewModelScope.launch {
            try {
                // Enviar mensagem via REST para o backend (mantido para persistência no MongoDB)
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

                // Opcional: Publicar a mensagem no Firebase Realtime Database para tempo real
                // Isso pode ser feito no backend Spring Boot após salvar no MongoDB, ou aqui no cliente.
                // Se feito no backend, o listener do Firebase no cliente já capturará a mensagem.
                // Se feito aqui, certifique-se de que o ID da mensagem seja consistente com o backend.
                // Por simplicidade, vamos assumir que o backend irá publicar no Firebase após salvar.
                // O listener acima já irá capturar a mensagem quando ela for adicionada pelo backend.

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Falha ao enviar mensagem via REST", e)
            }
        }
    }

    private fun ChatMessageDTO.toUiMessage(currentUserId: String): ChatMessage {
        val isUser = senderId == currentUserId
        return ChatMessage(
            id = id,
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

    override fun onCleared() {
        disconnectFirebaseChatListener()
        super.onCleared()
    }
}
package br.com.fiap.wtchat.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.wtchat.api.RetrofitInstance
import br.com.fiap.wtchat.api.dto.ChatMessageDTO
import br.com.fiap.wtchat.auth.SessionManager
import br.com.fiap.wtchat.model.ChatUiModel
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

class ChatViewModel : ViewModel() {

    private var conversationId: String? = null
    private val seenIds = mutableSetOf<String>()
    private var chatDatabaseRef: DatabaseReference? = null
    private var chatEventListener: ChildEventListener? = null

    private val _messages = mutableStateListOf<ChatUiModel>()
    val messages: SnapshotStateList<ChatUiModel> = _messages

    fun connect(convId: String) {
        conversationId = convId
        _messages.clear()
        seenIds.clear()
        disconnectFirebaseChatListener()

        val session = SessionManager.session ?: return

        viewModelScope.launch {
            try {
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

        connectFirebaseChatListener(convId, session.userId)
    }

    private fun connectFirebaseChatListener(convId: String, currentUserId: String) {
        chatDatabaseRef = FirebaseDatabase.getInstance().getReference("chats").child(convId)

        chatEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val messageDTO = snapshot.getValue(ChatMessageDTO::class.java)
                messageDTO?.let { dto ->
                    val mid = dto.id ?: snapshot.key
                    if (mid != null && mid !in seenIds) {
                        seenIds.add(mid)
                        _messages.add(dto.toUiMessage(currentUserId))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
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
                val removedMessageId = snapshot.key
                removedMessageId?.let { mid ->
                    _messages.removeIf { it.id == mid }
                    seenIds.remove(mid)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

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

                withContext(Dispatchers.IO) {
                    RetrofitInstance.api.sendMessage(auth, cid, request)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Falha ao enviar mensagem via REST", e)
            }
        }
    }

    private fun ChatMessageDTO.toUiMessage(currentUserId: String): ChatUiModel {
        val isUser = senderId == currentUserId
        return ChatUiModel(
            id = id,
            messageText = content,
            isSentByUser = isUser,
            formattedTime = formatSentAt(sentAt)
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

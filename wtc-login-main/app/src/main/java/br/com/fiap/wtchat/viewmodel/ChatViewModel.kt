/**
 * Chat: histórico REST + tempo real via STOMP (`/topic/conversation/{id}`).
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
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class ChatMessage(
    val id: String?,
    val text: String,
    val isUser: Boolean,
    val timestamp: String
)

class ChatViewModel : ViewModel() {

    private var conversationId: String? = null
    private val seenIds = mutableSetOf<String>()
    private val gson = Gson()
    private val rxDispose = CompositeDisposable()
    private var stomp: Stomp? = null

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: SnapshotStateList<ChatMessage> = _messages

    fun connect(convId: String) {
        conversationId = convId
        _messages.clear()
        seenIds.clear()
        disconnectStomp()

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
                Log.e("ChatViewModel", "Falha ao carregar mensagens", e)
            }
        }

        connectStomp(convId)
    }

    private fun connectStomp(convId: String) {
        val session = SessionManager.session ?: return
        try {
            stomp = Stomp.over(Stomp.ConnectionProvider.OKHTTP, RetrofitInstance.wsNativeUrl())
            rxDispose.add(
                stomp!!.lifecycle()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { event ->
                            if (event.type == LifecycleEvent.Type.OPENED) {
                                rxDispose.add(
                                    stomp!!.topic("/topic/conversation/$convId")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                            { stompMessage ->
                                                try {
                                                    val dto = gson.fromJson(stompMessage.payload, ChatMessageDTO::class.java)
                                                    val mid = dto.id ?: return@subscribe
                                                    if (mid in seenIds) return@subscribe
                                                    seenIds.add(mid)
                                                    _messages.add(dto.toUiMessage(session.userId))
                                                } catch (e: Exception) {
                                                    Log.e("ChatViewModel", "STOMP JSON", e)
                                                }
                                            },
                                            { err -> Log.e("ChatViewModel", "STOMP topic", err) }
                                        )
                                )
                            }
                        },
                        { err -> Log.e("ChatViewModel", "STOMP lifecycle", err) }
                    )
            )
            stomp!!.connect()
        } catch (e: Exception) {
            Log.e("ChatViewModel", "STOMP connect", e)
        }
    }

    private fun disconnectStomp() {
        rxDispose.clear()
        stomp?.disconnect()
        stomp = null
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
                saved.id?.let { seenIds.add(it) }
                _messages.add(saved.toUiMessage(session.userId))
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Falha ao enviar mensagem", e)
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
        disconnectStomp()
        super.onCleared()
    }
}

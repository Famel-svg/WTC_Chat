/**
 * Contrato Retrofit alinhado ao backend `wtc-crm-backend`.
 * Chamadas autenticadas devem passar `Authorization: Bearer <token>` (ver [br.com.fiap.wtchat.auth.SessionManager]).
 */
package br.com.fiap.wtchat.api

import br.com.fiap.wtchat.api.dto.ChatConversationDTO
import br.com.fiap.wtchat.api.dto.ChatMessageDTO
import br.com.fiap.wtchat.api.dto.LoginRequestDTO
import br.com.fiap.wtchat.api.dto.LoginResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface WtcApiService {

    /** POST `/api/auth/login` — retorna JWT e dados do usuário. */
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDTO): LoginResponseDTO

    /** GET `/api/chat/conversations` — lista conversas do usuário autenticado. */
    @GET("api/chat/conversations")
    suspend fun getConversations(
        @Header("Authorization") authorization: String
    ): List<ChatConversationDTO>

    /** GET `/api/chat/conversations/{id}/messages` — histórico de mensagens. */
    @GET("api/chat/conversations/{id}/messages")
    suspend fun getMessages(
        @Header("Authorization") authorization: String,
        @Path("id") conversationId: String
    ): List<ChatMessageDTO>

    /** POST `/api/chat/conversations/{id}/messages` — envia mensagem (REST; WebSocket é opcional). */
    @POST("api/chat/conversations/{id}/messages")
    suspend fun sendMessage(
        @Header("Authorization") authorization: String,
        @Path("id") conversationId: String,
        @Body request: ChatMessageDTO
    ): ChatMessageDTO
}


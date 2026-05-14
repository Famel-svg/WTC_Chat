/**
 * Contrato Retrofit alinhado ao backend `wtc-crm-backend`.
 * Chamadas autenticadas devem passar `Authorization: Bearer <token>` (ver [br.com.fiap.wtchat.auth.SessionManager]).
 */
package br.com.fiap.wtchat.api

import br.com.fiap.wtchat.api.dto.ChatConversationDTO
import br.com.fiap.wtchat.api.dto.ChatMessageDTO
import br.com.fiap.wtchat.api.dto.CampaignDTO
import br.com.fiap.wtchat.api.dto.ClientDTO
import br.com.fiap.wtchat.api.dto.LoginRequestDTO
import br.com.fiap.wtchat.api.dto.LoginResponseDTO
import br.com.fiap.wtchat.api.dto.MediaUploadResponseDTO
import br.com.fiap.wtchat.api.dto.Profile360DTO
import br.com.fiap.wtchat.api.dto.SegmentDTO
import br.com.fiap.wtchat.api.dto.TaskDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    /** PUT `/api/auth/users/{id}/fcm-token` — registra token FCM do dispositivo (corpo text/plain). */
    @PUT("api/auth/users/{id}/fcm-token")
    suspend fun updateFcmToken(
        @Path("id") userId: String,
        @Body body: RequestBody
    )

    /** GET `/api/tasks` — lista tarefas do CRM. */
    @GET("api/tasks")
    suspend fun getTasks(
        @Header("Authorization") authorization: String
    ): List<TaskDTO>

    /** POST `/api/tasks` — cria uma tarefa no backend. */
    @POST("api/tasks")
    suspend fun createTask(
        @Header("Authorization") authorization: String,
        @Body request: TaskDTO
    ): TaskDTO

    /** POST `/api/segments` — cria um grupo/segmento de clientes. */
    @POST("api/segments")
    suspend fun createSegment(
        @Header("Authorization") authorization: String,
        @Body request: SegmentDTO
    ): SegmentDTO

    /** POST `/api/campaigns` — cria uma campanha/disparo/promoção. */
    @POST("api/campaigns")
    suspend fun createCampaign(
        @Header("Authorization") authorization: String,
        @Body request: CampaignDTO
    ): CampaignDTO

    @GET("api/clients")
    suspend fun getClients(
        @Header("Authorization") authorization: String
    ): List<ClientDTO>

    @POST("api/clients")
    suspend fun createClient(
        @Header("Authorization") authorization: String,
        @Body request: ClientDTO
    ): ClientDTO

    @PUT("api/clients/{id}")
    suspend fun updateClient(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Body request: ClientDTO
    ): ClientDTO

    @DELETE("api/clients/{id}")
    suspend fun deleteClient(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    )

    @GET("api/profile360/{clientId}")
    suspend fun getProfile360(
        @Header("Authorization") authorization: String,
        @Path("clientId") clientId: String
    ): Profile360DTO

    @GET("api/segments")
    suspend fun getSegments(
        @Header("Authorization") authorization: String
    ): List<SegmentDTO>

    @GET("api/campaigns")
    suspend fun getCampaigns(
        @Header("Authorization") authorization: String
    ): List<CampaignDTO>

    @Multipart
    @POST("api/media/upload")
    suspend fun uploadMedia(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Query("folder") folder: String = "chat"
    ): MediaUploadResponseDTO
}


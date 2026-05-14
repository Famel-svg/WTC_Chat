package br.com.fiap.wtchat.api.dto

data class Profile360DTO(
    val client: ClientDTO? = null,
    val recentMessages: List<ChatMessageDTO> = emptyList(),
    val recentCampaigns: List<CampaignDTO> = emptyList(),
    val openTasks: List<TaskDTO> = emptyList()
)

package br.com.fiap.wtchat.api.dto

data class CampaignDTO(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val deeplink: String? = null,
    val mediaUrl: String? = null,
    val targetSegmentId: String? = null,
    val status: String? = null,
    val scheduledAt: String? = null,
    val sentAt: String? = null,
    val createdAt: String? = null,
    val recipientCount: Int = 0
)

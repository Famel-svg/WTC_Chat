package br.com.fiap.wtccrm.campaigns.dto;

import java.time.Instant;

public record CampaignDTO(
        String id,
        String title,
        String description,
        String deeplink,
        String mediaUrl,
        String targetSegmentId,
        String status,
        Instant scheduledAt,
        Instant sentAt,
        Instant createdAt,
        int recipientCount) {
}

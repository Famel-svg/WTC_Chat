package br.com.fiap.wtccrm.notifications.dto;

import java.util.List;
import java.util.Map;

public record PushNotificationDTO(
        String targetToken,
        List<String> tokens,
        String title,
        String body,
        Map<String, String> data) {
}

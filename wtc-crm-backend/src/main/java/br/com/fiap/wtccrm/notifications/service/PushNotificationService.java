package br.com.fiap.wtccrm.notifications.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PushNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationService.class);

    public void sendPush(String fcmToken, String title, String body, Map<String, String> data) {
        if (!StringUtils.hasText(fcmToken)) {
            return;
        }
        if (FirebaseApp.getApps().isEmpty()) {
            LOGGER.warn("FCM: Firebase Admin não inicializado; push não enviado.");
            return;
        }
        try {
            Message.Builder builder = Message.builder()
                    .setToken(fcmToken.trim())
                    .setNotification(Notification.builder()
                            .setTitle(title != null ? title : "WTC")
                            .setBody(body != null ? body : "")
                            .build());
            if (data != null) {
                builder.putAllData(data);
            }
            String id = FirebaseMessaging.getInstance().send(builder.build());
            LOGGER.debug("FCM enviado messageId={}", id);
        } catch (FirebaseMessagingException e) {
            LOGGER.warn("FCM falhou: {} — {}", e.getMessagingErrorCode(), e.getMessage());
        }
    }

    public void sendPushToSegment(String segmentId, String title, String body) {
        LOGGER.info("sendPushToSegment segment={} — use envio por tokens após resolver clientes", segmentId);
    }
}

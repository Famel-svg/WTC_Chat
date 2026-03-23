package br.com.fiap.wtccrm.notifications.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationService.class);

    public void sendPush(String fcmToken, String title, String body) {
        LOGGER.info("Push requested token={} title={} body={}", fcmToken, title, body);
    }

    public void sendPushToSegment(String segmentId, String title, String body) {
        LOGGER.info("Segment push requested segment={} title={} body={}", segmentId, title, body);
    }
}

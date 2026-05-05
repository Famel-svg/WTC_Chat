package br.com.fiap.wtccrm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

/**
 * Inicializa o Firebase Admin SDK se {@code firebase-service-account.json} existir no classpath.
 * {@code google.cloud.project-id} (ex.: do .env) sobrescreve o projeto quando definido.
 */
@Configuration
public class FirebaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${google.cloud.project-id:}")
    private String googleCloudProjectId;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }
        ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
        if (!resource.exists()) {
            LOGGER.info("Firebase Admin: arquivo firebase-service-account.json não encontrado no classpath; push omitido.");
            return;
        }
        FirebaseOptions.Builder builder = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()));
        if (StringUtils.hasText(googleCloudProjectId)) {
            builder.setProjectId(googleCloudProjectId.trim());
            LOGGER.info("Firebase Admin: projectId definido a partir da configuração.");
        }
        FirebaseApp.initializeApp(builder.build());
    }
}

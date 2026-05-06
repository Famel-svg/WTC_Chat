package br.com.fiap.wtccrm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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

    @Value("${firebase.config.path:classpath:wtc-chat-81c38-d85c9ae8335d.json}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }
        Resource resource = resolveResource();
        if (!resource.exists()) {
            LOGGER.warn("Firebase Admin: arquivo de credenciais não encontrado em {}; Firestore/FCM indisponíveis.", firebaseConfigPath);
            return;
        }
        try (InputStream credentials = resource.getInputStream()) {
            FirebaseOptions.Builder builder = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentials));
            if (StringUtils.hasText(googleCloudProjectId)) {
                builder.setProjectId(googleCloudProjectId.trim());
                LOGGER.info("Firebase Admin: projectId definido a partir da configuração.");
            }
            FirebaseApp.initializeApp(builder.build());
        }
    }

    @Bean
    public Firestore firestore() throws IOException {
        initializeFirebase();
        if (FirebaseApp.getApps().isEmpty()) {
            throw new IllegalStateException("Firebase Admin não inicializado. Gere uma service account do projeto wtc-chat-e9532 e salve em src/main/resources/wtc-chat-e9532-service-account.json.");
        }
        return FirestoreClient.getFirestore();
    }

    private Resource resolveResource() {
        if (StringUtils.hasText(firebaseConfigPath) && firebaseConfigPath.startsWith("classpath:")) {
            return new ClassPathResource(firebaseConfigPath.substring("classpath:".length()));
        }
        return new FileSystemResource(firebaseConfigPath);
    }
}

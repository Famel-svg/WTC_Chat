package br.com.fiap.wtchat.config

import br.com.fiap.wtchat.BuildConfig

/**
 * Valores do `.env` na raiz do repositório (via [BuildConfig]), para uso futuro com Firebase Client SDK / APIs Google.
 */
object FirebaseEnv {
    val projectDisplayName: String get() = BuildConfig.FIREBASE_PROJECT_DISPLAY_NAME
    val googleCloudProjectId: String get() = BuildConfig.GOOGLE_CLOUD_PROJECT_ID
    val googleCloudProjectNumber: String get() = BuildConfig.GOOGLE_CLOUD_PROJECT_NUMBER
    val webApiKey: String get() = BuildConfig.FIREBASE_WEB_API_KEY
}

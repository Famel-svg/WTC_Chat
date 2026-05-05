package br.com.fiap.wtchat.fcm

import br.com.fiap.wtchat.api.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Obtém o token FCM e envia ao backend (`PUT /api/auth/users/{id}/fcm-token`).
 */
object FcmRegistrar {

    fun registerAfterLogin(scope: CoroutineScope, userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) return@addOnCompleteListener
            val token = task.result ?: return@addOnCompleteListener
            scope.launch(Dispatchers.IO) {
                try {
                    RetrofitInstance.api.updateFcmToken(userId, token.toRequestBody("text/plain".toMediaType()))
                } catch (_: Exception) {
                    // Ignorado: sem Firebase configurado ou backend offline
                }
            }
        }
    }
}

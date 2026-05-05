package br.com.fiap.wtchat.fcm

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import br.com.fiap.wtchat.R

/**
 * Notificações em background quando chega push FCM (campanha ou mensagem).
 */
class WtcFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: "WTC"
        val body = message.notification?.body ?: message.data["body"] ?: ""

        val mgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setChannelId(CHANNEL_ID)
            .setSmallIcon(R.mipmap.wtc_company)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()
        mgr.notify(message.messageId?.hashCode() ?: System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val CHANNEL_ID = "wtc_fcm"
    }
}

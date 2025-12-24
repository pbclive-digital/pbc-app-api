package com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import com.kavi.pbc.live.com.kavi.pbc.live.integration.PushNotificationContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.FirebaseIntegration

class FirebasePushNotification: PushNotificationContract {

    companion object {
        const val GENERAL_CHANNEL_ID = "GENERAL_CHANNEL_ID"
        const val EVENT_CHANNEL_ID = "EVENT_CHANNEL_ID"
        const val NEWS_CHANNEL_ID = "NEWS_CHANNEL_ID"
        const val BROADCAST_CHANNEL_ID = "BROADCAST_CHANNEL_ID"
        const val APPOINTMENT_CHANNEL_ID = "APPOINTMENT_CHANNEL_ID"

        var shared: FirebasePushNotification = FirebasePushNotification()
    }

    override fun sendNotificationToToken(
        title: String,
        message: String,
        token: String,
        data: Map<String, String>
    ) {
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(message)
            .build()

        val notificationMessage = Message.builder()
            .setToken(token)
            .setNotification(notification)
            .putAllData(data)
            .build()

        val firebaseApp = FirebaseIntegration.shared.getFirebaseApp()
        firebaseApp?.let {
            FirebaseMessaging.getInstance(it).send(notificationMessage)
        }
    }

    override fun sendNotificationToMultipleTokens(
        title: String,
        message: String,
        tokens: List<String>,
        data: Map<String, String>
    ) {
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(message)
            .build()

        val notificationMessage = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setNotification(notification)
            .putAllData(data)
            .build()

        val firebaseApp = FirebaseIntegration.shared.getFirebaseApp()
        firebaseApp?.let {
            FirebaseMessaging.getInstance(it).sendEachForMulticast(notificationMessage)
        }
    }

    override fun sendNotificationToTopic(
        title: String,
        message: String,
        topic: String,
        data: Map<String, String>
    ) {
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(message)
            .build()

        val notificationMessage = Message.builder()
            .setTopic(topic)
            .setNotification(notification)
            .putAllData(data)
            .build()

        val firebaseApp = FirebaseIntegration.shared.getFirebaseApp()
        firebaseApp?.let {
            FirebaseMessaging.getInstance(it).send(notificationMessage)
        }
    }
}
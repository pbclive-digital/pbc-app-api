package com.kavi.pbc.live.com.kavi.pbc.live.integration

interface PushNotificationContract {
    fun sendNotificationToToken(title: String, message: String, token: String, data: Map<String, String> = emptyMap())
    fun sendNotificationToMultipleTokens(title: String, message: String, tokens: List<String>, data: Map<String, String> = emptyMap())
    fun sendNotificationToTopic(title: String, message: String, topic: String, data: Map<String, String> = emptyMap())
}
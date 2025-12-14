package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.notification.FirebasePushNotification
import com.kavi.pbc.live.data.model.broadcast.BroadcastMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BroadcastService {

    @Autowired
    lateinit var logger: AppLogger

    @Autowired
    lateinit var pushTokenService: PushTokenService

    fun broadcastMessage(broadCastMessage: BroadcastMessage): ResponseEntity<BaseResponse<String>>? {
        pushTokenService.getAllPushTokens()
        val data = mapOf(
            "CHANNEL" to FirebasePushNotification.BROADCAST_CHANNEL_ID,
        )

        FirebasePushNotification.shared.sendNotificationToMultipleTokens(
            title = "IMPORTANT: ${broadCastMessage.title}",
            message = broadCastMessage.message,
            tokens = pushTokenService.getAllPushTokens(),
            data = data
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                "Broadcast Message",
                null))
    }
}
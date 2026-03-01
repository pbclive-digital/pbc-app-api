package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.data.dto.BaseResponse
import com.kavi.pbc.live.api.data.dto.Status
import com.kavi.pbc.live.api.service.PushNotificationService
import com.kavi.pbc.live.api.service.EmailService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.broadcast.EmailBroadcastMessage
import com.kavi.pbc.live.data.model.broadcast.NotificationMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/broadcast")
class BroadcastController(
    private val emailService: EmailService,
    private val pushNotificationService: PushNotificationService
) {

    @Autowired
    lateinit var logger: AppLogger

    @PostMapping("/notification")
    fun broadcastNotification(@Validated @RequestBody broadCastMessage: NotificationMessage):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/broadcast/notification]", BroadcastController::class.java)

        val response = pushNotificationService.broadcastNotification(broadCastMessage)
        logger.printResponseInfo(response, BroadcastController::class.java)

        return response
    }

    @PostMapping("/email")
    fun broadcastEmail(@Validated @RequestBody broadCastMessage: EmailBroadcastMessage):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/broadcast/email]", BroadcastController::class.java)

        //val response = emailService.sendBroadcastEmail(broadCastMessage)
        emailService.sendBroadcastEmail(broadCastMessage)
        //logger.printResponseInfo(response, BroadcastController::class.java)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                "Broadcast Email",
                null))

        //return response
    }
}
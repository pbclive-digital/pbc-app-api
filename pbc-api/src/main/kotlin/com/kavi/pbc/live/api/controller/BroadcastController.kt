package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.BroadcastService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.broadcast.BroadcastMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/broadcast")
class BroadcastController(private val broadcastService: BroadcastService) {

    @Autowired
    lateinit var logger: AppLogger

    @PostMapping("/message")
    fun broadcastMessage(@Validated @RequestBody broadCastMessage: BroadcastMessage):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/broadcast/message]", BroadcastController::class.java)

        val response = broadcastService.broadcastMessage(broadCastMessage)
        logger.printResponseInfo(response, BroadcastController::class.java)

        return response
    }
}
package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.EventService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.event.Event
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/event")
class EventController(private val eventService: EventService) {

    @Autowired
    lateinit var logger: AppLogger

    @PostMapping("/create")
    fun createEvent(@Valid @RequestBody event: Event): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/event/create]", EventController::class.java)

        val response = eventService.createEvent(event)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @PostMapping("/add-event-image")
    fun addEventImage(@RequestParam("eventImage") eventImage: MultipartFile): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/event/add-event-image]", EventController::class.java)

        val response = eventService.addEventImage(eventImage)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @GetMapping("/get/upcoming")
    fun getAllUpcomingEvents(): ResponseEntity<BaseResponse<List<Event>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/event/get/upcoming]", EventController::class.java)

        val response = eventService.getUpcomingEvents()
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @GetMapping("/get/past")
    fun getAllPassEvents(): ResponseEntity<BaseResponse<List<Event>>>?? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/event/get/past]", EventController::class.java)

        val response = eventService.getPastEvents()
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }
}
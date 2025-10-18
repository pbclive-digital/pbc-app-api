package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.EventService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.event.Event
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @PostMapping("/add-event-image/{event-name}/{event-date}")
    fun addEventImage(@PathVariable(value = "event-name") eventName: String,
                      @PathVariable(value = "event-date") eventDateTimestamp: Long, @RequestParam("eventImage") eventImage: MultipartFile): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/event/add-event-image/$eventName/$eventDateTimestamp]", EventController::class.java)

        val response = eventService.addEventImage(eventImage, eventName, eventDateTimestamp)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @GetMapping("/get/draft")
    fun getAllDraftEvents(): ResponseEntity<BaseResponse<List<Event>>>?? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/event/get/draft]", EventController::class.java)

        val response = eventService.getDraftEvents()
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

    @DeleteMapping("/delete/{event-id}")
    fun deleteEvent(@PathVariable(value = "event-id") eventId: String):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: DELETE:[/event/delete/event//$eventId]", EventController::class.java)

        val response = eventService.deleteGivenEvent(eventId)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }
}
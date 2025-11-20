package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.EventService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.event.Event
import com.kavi.pbc.live.data.model.event.potluck.EventPotluck
import com.kavi.pbc.live.data.model.event.potluck.EventPotluckContributor
import com.kavi.pbc.live.data.model.event.register.EventRegistration
import com.kavi.pbc.live.data.model.event.register.EventRegistrationItem
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @PostMapping("/add/image/{event-name}")
    fun addEventImage(@PathVariable(value = "event-name") eventName: String, @RequestParam("eventImage") eventImage: MultipartFile): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/event/add/image/$eventName]", EventController::class.java)

        val response = eventService.addEventImage(eventImage, eventName)
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

    @PutMapping("/update/{event-id}")
    fun updateEvent(@PathVariable(value = "event-id") eventId: String,
                    @Valid @RequestBody event: Event): ResponseEntity<BaseResponse<Event>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: PUT: [/event/update/$eventId]", EventController::class.java)

        val response = eventService.updateEvent(eventId, event)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @PutMapping("/put/publish/{event-id}")
    fun publishDraftEvent(@PathVariable(value = "event-id") eventId: String, @Valid @RequestBody event: Event): ResponseEntity<BaseResponse<Event>>?? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: PUT: [/event/put/publish/$eventId]", EventController::class.java)

        val response = eventService.publishDraftEvent(eventId, event)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @DeleteMapping("/delete/{event-id}")
    fun deleteEvent(@PathVariable(value = "event-id") eventId: String):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: DELETE:[/event/delete/event/$eventId]", EventController::class.java)

        val response = eventService.deleteGivenEvent(eventId)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @PostMapping("/register/{event-id}")
    fun registerToEvent(@PathVariable(value = "event-id") eventId: String, @Valid @RequestBody eventRegItem: EventRegistrationItem):
            ResponseEntity<BaseResponse<EventRegistration>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST:[/event/register/$eventId]", EventController::class.java)

        val response = eventService.registerToEvent(eventId, eventRegItem)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @DeleteMapping("/unregister/{event-id}/{user-id}")
    fun unregisterFromEvent(@PathVariable(value = "event-id") eventId: String, @PathVariable(value = "user-id") userId: String):
            ResponseEntity<BaseResponse<EventRegistration>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST:[/event/unregister/$eventId/$userId]", EventController::class.java)

        val response = eventService.unregisterFromEvent(eventId, userId)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @GetMapping("/get/registration/{event-id}")
    fun getEventRegistration(@PathVariable(value = "event-id") eventId: String): ResponseEntity<BaseResponse<EventRegistration>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET:[/event/get/registration/$eventId]", EventController::class.java)

        val response = eventService.getEventRegistrationRecord(eventId)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @GetMapping("/get/potluck/{event-id}")
    fun getEventPotluck(@PathVariable(value = "event-id") eventId: String): ResponseEntity<BaseResponse<EventPotluck>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET:[/event/get/potluck/$eventId]", EventController::class.java)

        val response = eventService.getEventPotluckRecord(eventId)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @PostMapping("/potluck/sign-up/{event-id}/{potluck-item-id}")
    fun signUpToPotluck(@PathVariable(value = "event-id") eventId: String,
                        @PathVariable(value = "potluck-item-id") potluckItemId: String,
                        @Valid @RequestBody contributor: EventPotluckContributor):
            ResponseEntity<BaseResponse<EventPotluck>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST:[/event/potluck/sign-up/$eventId/$potluckItemId]", EventController::class.java)

        val response = eventService.signUpGivenContributorToPotluckItem(eventId, potluckItemId, contributor)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }

    @DeleteMapping("/potluck/sign-out/{event-id}/{potluck-item-id}/{contributor-id}")
    fun signOutFromPotluck(@PathVariable(value = "event-id") eventId: String,
                           @PathVariable(value = "potluck-item-id") potluckItemId: String,
                           @PathVariable(value = "contributor-id") contributorId: String):
            ResponseEntity<BaseResponse<EventPotluck>>? {

        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: DELETE:[/event/potluck/sign-out/$eventId/$potluckItemId/$contributorId]", EventController::class.java)

        val response = eventService.signOutGivenContributorFromPotluckItem(eventId, potluckItemId, contributorId)
        logger.printResponseInfo(response, EventController::class.java)

        return response
    }
}
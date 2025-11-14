package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.cdn.FirebaseCDNConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.cdn.FirebaseStorage
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.event.Event
import com.kavi.pbc.live.data.model.event.EventStatus
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.data.model.event.potluck.EventPotluck
import com.kavi.pbc.live.data.model.event.potluck.EventPotluckContributor
import com.kavi.pbc.live.data.model.event.potluck.EventPotluckItem
import com.kavi.pbc.live.data.model.event.register.EventRegistration
import com.kavi.pbc.live.data.model.event.register.EventRegistrationItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.Date
import java.util.UUID

@Service
class EventService {

    @Autowired
    lateinit var logger: AppLogger
    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun createEvent(event: Event): ResponseEntity<BaseResponse<String>>? {

        val eventCreationRes = datastoreRepositoryContract.createEntity(DatastoreConstant.EVENT_COLLECTION, event.id, event)

        if (event.registrationRequired) {
            val eventRegistration = EventRegistration(event.id, event.openSeatCount!!)
            datastoreRepositoryContract.createEntity(DatastoreConstant.EVENT_REGISTRATION_COLLECTION, eventRegistration.id, eventRegistration)
        }

        if (event.potluckAvailable) {
            if (!event.potluckItemList.isNullOrEmpty()) {
                val potluckItemList = mutableListOf<EventPotluckItem>()
                event.potluckItemList?.forEach { potluckItem ->
                    potluckItemList.add(EventPotluckItem(UUID.randomUUID().toString(),
                        potluckItem.itemName, potluckItem.itemCount))
                }

                val eventPotluck = EventPotluck(event.id, potluckItemList)
                datastoreRepositoryContract.createEntity(DatastoreConstant.EVENT_POTLUCK_COLLECTION, eventPotluck.id, eventPotluck)
            }
        }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                eventCreationRes, null))
    }

    fun addEventImage(eventImage: MultipartFile, eventName: String): ResponseEntity<BaseResponse<String>>? {

        val formatedEventName = eventName.replace(" ", "_").replace("-", "_")
        val createdTimestamp = System.currentTimeMillis()
        val formatFileName = "${FirebaseCDNConstant.EVENT_DIR_NAME}/$createdTimestamp:$formatedEventName"

        val url = FirebaseStorage.getInstance().uploadFile(
            eventImage.bytes, formatFileName, eventImage.contentType)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                url, null))
    }

    fun getDraftEvents(): ResponseEntity<BaseResponse<List<Event>>>? {
        val properties = mapOf(
            "eventStatus" to EventStatus.DRAFT
        )
        val orderBy = mapOf(
            "property" to "eventDate",
            "direction" to "ASC"
        )

        val finalDraftEventList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.EVENT_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = Event::class.java
        )

        return if (finalDraftEventList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, finalDraftEventList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun getUpcomingEvents(): ResponseEntity<BaseResponse<List<Event>>>? {
        val properties = mapOf(
            "eventStatus" to EventStatus.PUBLISHED
        )
        val orderBy = mapOf(
            "property" to "eventDate",
            "direction" to "ASC"
        )

        val finalUpcomingEventList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.EVENT_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = Event::class.java
        )

        return if (finalUpcomingEventList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, finalUpcomingEventList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun getPastEvents(): ResponseEntity<BaseResponse<List<Event>>>? {
        val properties = mapOf(
            "eventStatus" to EventStatus.PASSED
        )
        val orderBy = mapOf(
            "property" to "eventDate",
            "direction" to "DESC"
        )

        val finalPassedEventList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.EVENT_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = Event::class.java
        )

        return if (finalPassedEventList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, finalPassedEventList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun updateEventStatusAccordingToDate(): String? {
        val properties = mapOf(
            "eventStatus" to EventStatus.PUBLISHED
        )
        val lessThanMap = mapOf(
            "eventDate" to System.currentTimeMillis()
        )

        val dueEventList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.EVENT_COLLECTION,
            propertiesMap = properties,
            lessThanMap = lessThanMap,
            className = Event::class.java
        )

        dueEventList.forEach { dueEvent ->
            dueEvent.eventStatus = EventStatus.PASSED
            datastoreRepositoryContract.updateEntity(
                entityCollection = DatastoreConstant.EVENT_COLLECTION,
                entityId = dueEvent.id,
                entity = dueEvent
            )
        }

        return if (dueEventList.isNotEmpty()) {
            "Event status updated as PASSED in [${dueEventList.size}] events."
        } else {
            "No Events found that due from Date: [${Date()}]"
        }
    }

    fun updateEvent(eventId: String, event: Event): ResponseEntity<BaseResponse<Event>>? {

        datastoreRepositoryContract.updateEntity(DatastoreConstant.EVENT_COLLECTION, eventId, event)

        if (event.registrationRequired) {
            datastoreRepositoryContract.getEntityFromId(
                DatastoreConstant.EVENT_REGISTRATION_COLLECTION, entityId = eventId,
                EventRegistration::class.java
            )?.let {
                logger.printInfo("Record already available to the " +
                        "event:$eventId in ${DatastoreConstant.EVENT_REGISTRATION_COLLECTION} collection.", EventService::class.java)
            }?: run {
                val eventRegistration = EventRegistration(event.id, event.openSeatCount!!)
                datastoreRepositoryContract
                    .createEntity(DatastoreConstant.EVENT_REGISTRATION_COLLECTION,
                        eventRegistration.id, eventRegistration)
            }
        }

        if (event.potluckAvailable) {
            datastoreRepositoryContract.getEntityFromId(
                DatastoreConstant.EVENT_POTLUCK_COLLECTION, entityId = eventId,
                EventPotluck::class.java
            )?.let {
                logger.printInfo("Record already available to the " +
                        "event:$eventId in ${DatastoreConstant.EVENT_POTLUCK_COLLECTION} collection.", EventService::class.java)
            }?: run {
                if (!event.potluckItemList.isNullOrEmpty()) {
                    val potluckItemList = mutableListOf<EventPotluckItem>()
                    event.potluckItemList?.forEach { potluckItem ->
                        potluckItemList.add(EventPotluckItem(UUID.randomUUID().toString(),
                            potluckItem.itemName, potluckItem.itemCount))
                    }

                    val eventPotluck = EventPotluck(event.id, potluckItemList)
                    datastoreRepositoryContract.createEntity(DatastoreConstant.EVENT_POTLUCK_COLLECTION, eventPotluck.id, eventPotluck)
                }
            }
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                event, null))
    }

    fun publishDraftEvent(eventId: String, event: Event): ResponseEntity<BaseResponse<Event>>? {
        event.eventStatus = EventStatus.PUBLISHED

        datastoreRepositoryContract.updateEntity(DatastoreConstant.EVENT_COLLECTION, eventId, event)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                event,
                null))
    }

    fun deleteGivenEvent(eventId: String): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.deleteEntity(DatastoreConstant.EVENT_COLLECTION, eventId),
                null))
    }

    fun registerToEvent(eventId: String, eventRegistrationItem: EventRegistrationItem): ResponseEntity<BaseResponse<EventRegistration>>? {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.EVENT_REGISTRATION_COLLECTION, eventId,
            EventRegistration::class.java)?.let { eventRegistration ->
                eventRegistration.registrationList.add(eventRegistrationItem)
            datastoreRepositoryContract.updateEntity(DatastoreConstant.EVENT_REGISTRATION_COLLECTION, eventRegistration.id, eventRegistration)

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse(Status.SUCCESS,
                    eventRegistration,
                    null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun unregisterFromEvent(eventId: String, userId: String): ResponseEntity<BaseResponse<EventRegistration>>? {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.EVENT_REGISTRATION_COLLECTION, eventId,
            EventRegistration::class.java)?.let { eventRegistration ->
            eventRegistration.registrationList.removeIf { it.participantUserId == userId }
            datastoreRepositoryContract.updateEntity(DatastoreConstant.EVENT_REGISTRATION_COLLECTION, eventRegistration.id, eventRegistration)

            return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(BaseResponse(Status.SUCCESS,
                    eventRegistration,
                    null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun getEventRegistrationRecord(eventId: String): ResponseEntity<BaseResponse<EventRegistration>>? {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.EVENT_REGISTRATION_COLLECTION, eventId,
            EventRegistration::class.java)?.let {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse(Status.SUCCESS,
                    it,
                    null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun getEventPotluckRecord(eventId: String): ResponseEntity<BaseResponse<EventPotluck>>? {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.EVENT_POTLUCK_COLLECTION, eventId,
            EventPotluck::class.java)?.let {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse(Status.SUCCESS,
                    it,
                    null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun signUpGivenContributorToPotluckItem(eventId: String, potluckItemId: String, contributor: EventPotluckContributor):
            ResponseEntity<BaseResponse<EventPotluck>>? {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.EVENT_POTLUCK_COLLECTION, eventId,
            EventPotluck::class.java)?.let { potluck ->
            val updatedPotluck = potluck.copy()

            val filteredPotluckItem = potluck.potluckItemList.filter { it.itemId == potluckItemId }

            if (filteredPotluckItem.isNotEmpty()) {
                val selectedPotluckItem = filteredPotluckItem[0]

                selectedPotluckItem.contributorList.add(contributor)

                updatedPotluck.potluckItemList.removeIf { it.itemId == potluckItemId }
                updatedPotluck.potluckItemList.add(selectedPotluckItem)

                datastoreRepositoryContract.updateEntity(DatastoreConstant.EVENT_POTLUCK_COLLECTION, updatedPotluck.id, updatedPotluck)

                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(BaseResponse(Status.SUCCESS,
                        updatedPotluck,
                        null))
            } else {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse(
                        Status.ERROR, null, listOf(
                            Error("${HttpStatus.NOT_FOUND} due to potluck item not found")
                        ))
                    )
            }
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error("${HttpStatus.NOT_FOUND} due to potluck not found"))
                ))
        }
    }
}
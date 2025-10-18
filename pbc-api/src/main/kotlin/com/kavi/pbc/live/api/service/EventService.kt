package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.cdn.FirebaseCDNConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.cdn.FirebaseStorage
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.event.Event
import com.kavi.pbc.live.data.model.event.EventStatus
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.Date

@Service
class EventService {

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun createEvent(event: Event): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.createEntity(DatastoreConstant.EVENT_COLLECTION, event.id, event), null))
    }

    fun addEventImage(eventImage: MultipartFile, eventName: String, eventDateTimestamp: Long): ResponseEntity<BaseResponse<String>>? {

        val formatedEventName = eventName.replace(" ", "_").replace("-", "_")
        val formatFileName = "${FirebaseCDNConstant.EVENT_DIR_NAME}/$eventDateTimestamp:$formatedEventName"

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
        println("Current Time: ${System.currentTimeMillis()}")
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
}
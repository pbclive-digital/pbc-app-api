package com.kavi.pbc.live.api.service

import com.kavi.droid.survey.api.dto.BaseResponse
import com.kavi.droid.survey.api.dto.Error
import com.kavi.droid.survey.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.firebase.repository.FirebaseDataRepository
import com.kavi.pbc.live.data.model.event.Event
import com.kavi.pbc.live.data.model.event.EventStatus
import com.kavi.pbc.live.data.repository.DataRepository
import com.kavi.pbc.live.data.repository.db.DBConstant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class EventService {

    private var dataRepository: DataRepository = FirebaseDataRepository()

    fun createEvent(event: Event): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                dataRepository.createEntity(DBConstant.EVENT_COLLECTION, event.id, event), null))
    }

    fun getUpcomingEvents(): ResponseEntity<BaseResponse<List<Event>>>? {
        val properties = mapOf(
            "eventStatus" to EventStatus.PUBLISHED
        )
        val orderBy = mapOf(
            "property" to "eventDate",
            "direction" to "DESC"
        )

        val finalUpcomingEventList = dataRepository.getEntityListFromProperties(
            entityCollection = DBConstant.EVENT_COLLECTION,
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

        val finalUpcomingEventList = dataRepository.getEntityListFromProperties(
            entityCollection = DBConstant.EVENT_COLLECTION,
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
}
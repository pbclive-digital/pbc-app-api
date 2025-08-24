package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.datastore.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.firebase.repository.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.DataConstant.DASHBOARD_PAGER_EVENT_COUND
import com.kavi.pbc.live.data.model.event.Event
import com.kavi.pbc.live.data.model.event.EventStatus
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class DashboardService {

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun getDashboardEvents(): ResponseEntity<BaseResponse<List<Event>>>? {

        val finalEventList: List<Event>
        val upcomingEventList = upcomingEvents()

        if (upcomingEventList.size < DASHBOARD_PAGER_EVENT_COUND) {
            val olderEventList = oldEvents(DASHBOARD_PAGER_EVENT_COUND - upcomingEventList.size)
            finalEventList = upcomingEventList + olderEventList
        } else {
            finalEventList = upcomingEventList
        }

        return if (finalEventList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, finalEventList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    private fun upcomingEvents(): MutableList<Event> {
        val properties = mapOf(
            "eventStatus" to EventStatus.PUBLISHED
        )
        val orderBy = mapOf(
            "property" to "eventDate",
            "direction" to "ASC"
        )

        return datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.EVENT_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = Event::class.java
        )
    }

    private fun oldEvents(limit: Int): MutableList<Event> {
        val properties = mapOf(
            "eventStatus" to EventStatus.PASSED
        )
        val orderBy = mapOf(
            "property" to "eventDate",
            "direction" to "DESC"
        )

        return datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.EVENT_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            limit = limit,
            className = Event::class.java
        )
    }
}
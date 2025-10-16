package com.kavi.pbc.live.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.DataConstant.DASHBOARD_PAGER_EVENT_COUND
import com.kavi.pbc.live.data.model.DailyQuote
import com.kavi.pbc.live.data.model.event.Event
import com.kavi.pbc.live.data.model.event.EventStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import com.fasterxml.jackson.core.type.TypeReference
import com.kavi.pbc.live.data.DataConstant
import com.kavi.pbc.live.data.model.news.News
import com.kavi.pbc.live.data.model.news.NewsStatus


@Service
class DashboardService (
    private val objectMapper: ObjectMapper
) {

    @Autowired
    private val resourceLoader: ResourceLoader? = null

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

    fun getDailyQuotes(): ResponseEntity<BaseResponse<List<DailyQuote>>>? {
        val allQuoteList: List<DailyQuote> = loadQuotes()

        val randomList = allQuoteList.shuffled().take(DataConstant.DAILY_QUOTE_COUNT)
        return ResponseEntity.ok(BaseResponse(Status.SUCCESS, randomList, null))
    }

    fun getNews(): ResponseEntity<BaseResponse<List<News>>> {
        val newsList = retrieveActiveNews()

        return if (newsList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, newsList, null))
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

    private fun loadQuotes(): List<DailyQuote> {
        val resource: Resource = resourceLoader!!.getResource("classpath:quotes/daily_quotes.json")
        resource.inputStream.use { inputStream ->
            return objectMapper.readValue(inputStream, object : TypeReference<List<DailyQuote>>() {})
        }
    }

    private fun retrieveActiveNews(): List<News> {
        val properties = mapOf(
            "newsStatus" to NewsStatus.ACTIVE
        )
        val orderBy = mapOf(
            "property" to "createdTime",
            "direction" to "DESC"
        )

        return datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.NEWS_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            limit = 3,
            className = News::class.java
        )
    }
}
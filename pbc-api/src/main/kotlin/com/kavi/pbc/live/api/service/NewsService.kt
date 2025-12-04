package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.news.News
import com.kavi.pbc.live.data.model.news.NewsStatus
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class NewsService {

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun createNews(news: News): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.createEntity(DatastoreConstant.NEWS_COLLECTION, news.id, news), null))
    }

    fun getDraftNews(): ResponseEntity<BaseResponse<List<News>>>? {
        val properties = mapOf(
            "newsStatus" to NewsStatus.DRAFT
        )
        val orderBy = mapOf(
            "property" to "createdTime",
            "direction" to "ASC"
        )

        val finalDraftNewsList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.NEWS_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = News::class.java
        )

        return if (finalDraftNewsList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, finalDraftNewsList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun getActiveNews(): ResponseEntity<BaseResponse<List<News>>>? {
        val properties = mapOf(
            "newsStatus" to NewsStatus.ACTIVE
        )
        val orderBy = mapOf(
            "property" to "createdTime",
            "direction" to "ASC"
        )

        val finalActiveNewsList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.NEWS_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = News::class.java
        )

        return if (finalActiveNewsList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, finalActiveNewsList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun updateNews(newsId: String, news: News): ResponseEntity<BaseResponse<News>> {
        datastoreRepositoryContract.updateEntity(DatastoreConstant.NEWS_COLLECTION,
            newsId, news)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                news, null))
    }

    fun deleteGivenNews(newsId: String): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.deleteEntity(DatastoreConstant.NEWS_COLLECTION, newsId),
                null))
    }
}
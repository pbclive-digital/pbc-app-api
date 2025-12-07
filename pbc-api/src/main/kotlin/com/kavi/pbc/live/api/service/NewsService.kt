package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.cdn.FirebaseCDNConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.cdn.FirebaseStorage
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.news.News
import com.kavi.pbc.live.data.model.news.NewsStatus
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.time.Instant
import java.util.Date

@Service
class NewsService {

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun createNews(news: News): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.createEntity(DatastoreConstant.NEWS_COLLECTION, news.id, news), null))
    }

    fun addNewsImage(newsImage: MultipartFile, newsTitle: String): ResponseEntity<BaseResponse<String>>? {
        val formatedNewsTitle = newsTitle.replace(" ", "_").replace("-", "_")
        val createdTimestamp = System.currentTimeMillis()
        val formatFileName = "${FirebaseCDNConstant.NEWS_DIR_NAME}/$createdTimestamp:$formatedNewsTitle"

        val url = FirebaseStorage.getInstance().uploadFile(
            newsImage.bytes, formatFileName, newsImage.contentType)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                url, null))
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
            "direction" to "DESC"
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

    fun getNewsById(newsId: String): ResponseEntity<BaseResponse<News>>? {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.NEWS_COLLECTION,
            newsId, News::class.java)?.let {
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS, it, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun publishDraftNews(newsId: String, news: News): ResponseEntity<BaseResponse<News>>? {
        news.newsStatus = NewsStatus.ACTIVE
        news.publishedTime = System.currentTimeMillis()

        datastoreRepositoryContract.updateEntity(DatastoreConstant.NEWS_COLLECTION, newsId, news)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                news,
                null))
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

    fun updateNewsStatusAccordingToDate(): String? {

        val oneMonthBefore = Instant.now().minus(Duration.ofDays(30)).toEpochMilli()

        val properties = mapOf(
            "newsStatus" to NewsStatus.ACTIVE
        )
        val lessThanMap = mapOf(
            "publishedTime" to oneMonthBefore
        )

        val oldNewsList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.NEWS_COLLECTION,
            propertiesMap = properties,
            lessThanMap = lessThanMap,
            className = News::class.java
        )

        oldNewsList.forEach { oldNews ->
            oldNews.newsStatus = NewsStatus.ARCHIVE
            datastoreRepositoryContract.updateEntity(
                entityCollection = DatastoreConstant.NEWS_COLLECTION,
                entityId = oldNews.id,
                entity = oldNews
            )
        }

        return if (oldNewsList.isNotEmpty()) {
            "News status updated as ARCHIVE in [${oldNewsList.size}] news."
        } else {
            "No News found that due from Date: [${Date()}]"
        }
    }
}
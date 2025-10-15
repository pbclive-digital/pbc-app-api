package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.news.News
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
}
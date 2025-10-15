package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.NewsService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.news.News
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/news")
class NewsController(private val newsService: NewsService) {

    @Autowired
    lateinit var logger: AppLogger

    @PostMapping("/create")
    fun createNews(@Valid @RequestBody news: News): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/news/create]", NewsController::class.java)

        val response = newsService.createNews(news)
        logger.printResponseInfo(response, NewsController::class.java)

        return response
    }
}
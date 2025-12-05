package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.NewsService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.news.News
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

    @GetMapping("/get/draft")
    fun getAllDraftNews(): ResponseEntity<BaseResponse<List<News>>>?? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/news/get/draft]", NewsController::class.java)

        val response = newsService.getDraftNews()
        logger.printResponseInfo(response, NewsController::class.java)

        return response
    }

    @GetMapping("/get/active")
    fun getAllActiveNews(): ResponseEntity<BaseResponse<List<News>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/news/get/active]", NewsController::class.java)

        val response = newsService.getActiveNews()
        logger.printResponseInfo(response, NewsController::class.java)

        return response
    }

    @PutMapping("/update/publish/{news-id}")
    fun publishDraftNews(@PathVariable(value = "news-id") newsId: String, @Valid @RequestBody news: News): ResponseEntity<BaseResponse<News>>?? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: PUT: [/news/put/publish/$newsId]", NewsController::class.java)

        val response = newsService.publishDraftNews(newsId, news)
        logger.printResponseInfo(response, NewsController::class.java)

        return response
    }

    @PutMapping("/update/{news-id}")
    fun updateNews(@PathVariable(value = "news-id") newsId: String,
                    @Valid @RequestBody news: News): ResponseEntity<BaseResponse<News>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: PUT: [/news/update/$newsId]", NewsController::class.java)

        val response = newsService.updateNews(newsId, news)
        logger.printResponseInfo(response, NewsController::class.java)

        return response
    }

    @DeleteMapping("/delete/{news-id}")
    fun deleteNews(@PathVariable(value = "news-id") newsId: String):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: DELETE:[/news/delete/$newsId]", NewsController::class.java)

        val response = newsService.deleteGivenNews(newsId)
        logger.printResponseInfo(response, NewsController::class.java)

        return response
    }
}
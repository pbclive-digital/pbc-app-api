package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.DashboardService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.DailyQuote
import com.kavi.pbc.live.data.model.event.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardController(private val dashboardService: DashboardService) {

    @Autowired
    lateinit var logger: AppLogger

    @GetMapping("/get/events")
    fun getDashboardEvents(): ResponseEntity<BaseResponse<List<Event>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/dashboard/get/events]", DashboardController::class.java)

        val response = dashboardService.getDashboardEvents()
        logger.printResponseInfo(response, DashboardController::class.java)

        return response
    }

    @GetMapping("/get/daily/quotes")
    fun getDashboardQuotes(): ResponseEntity<BaseResponse<List<DailyQuote>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/dashboard/get/daily/quote]", DashboardController::class.java)

        val response = dashboardService.getDailyQuotes()
        logger.printResponseInfo(response, DashboardController::class.java)

        return response
    }
}
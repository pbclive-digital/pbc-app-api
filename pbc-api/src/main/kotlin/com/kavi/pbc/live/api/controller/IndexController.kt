package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.AppProperties
import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.api.util.AppLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var logger: AppLogger

    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("appVersion", appProperties.appVersion)
        return "index"
    }

    @GetMapping("/wake-up")
    fun wakeUp(): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/wake-up]", IndexController::class.java)

        val response: ResponseEntity<BaseResponse<String>> = ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(BaseResponse(Status.SUCCESS, "Application wake-up", null))

        logger.printResponseInfo(response, IndexController::class.java)
        return response
    }
}
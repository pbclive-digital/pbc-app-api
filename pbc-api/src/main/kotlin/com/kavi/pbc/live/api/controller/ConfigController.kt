package com.kavi.pbc.live.api.controller

import com.kavi.droid.survey.api.dto.BaseResponse
import com.kavi.droid.survey.api.dto.Status
import com.kavi.pbc.live.api.service.ConfigService
import com.kavi.pbc.live.api.util.AppLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/config")
class ConfigController(private val configService: ConfigService) {

    @Autowired
    lateinit var logger: AppLogger

    @GetMapping("/get/{version}")
    fun getConfig(@PathVariable(value = "version") version: String): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/config/get/$version]", ConfigController::class.java)

        return ResponseEntity.ok(BaseResponse(Status.SUCCESS, "TEST VALUE", null))
    }
}
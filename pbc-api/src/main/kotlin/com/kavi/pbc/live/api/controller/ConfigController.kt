package com.kavi.pbc.live.api.controller

import com.kavi.droid.survey.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.ConfigService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.config.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/config")
class ConfigController(private val configService: ConfigService) {

    @Autowired
    lateinit var logger: AppLogger

    @GetMapping("/get/{version}")
    fun getConfig(@PathVariable(value = "version") version: String): ResponseEntity<BaseResponse<Config>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/config/get/$version]", ConfigController::class.java)

        val response = configService.getConfigByVersion(version)
        logger.printResponseInfo(response, ConfigController::class.java)

        return response
    }

    @PostMapping("/create/{version}")
    fun createConfig(@PathVariable(value = "version") version: String,
                     @Validated @RequestBody config: Config
    ): ResponseEntity<BaseResponse<String>>? {

        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/config/create/$version]", ConfigController::class.java)

        val response = configService.createConfigByVersion(version, config)
        logger.printResponseInfo(response, ConfigController::class.java)

        return response
    }
}
package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.api.service.ConfigService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.config.AppVersionStatus
import com.kavi.pbc.live.data.model.config.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
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

    @GetMapping("/get/app-support/status")
    fun getAppSupport(@RequestHeader("X-app-os") deviceOS: String?, @RequestHeader("X-app-version") appVersion: String?): ResponseEntity<BaseResponse<AppVersionStatus>>? {

        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/config/get/app-support/status]", ConfigController::class.java)

        if (deviceOS != null && appVersion != null) {
            val response = configService.isApiSupportForAppVersion(deviceOS, appVersion)

            logger.printResponseInfo(response, ConfigController::class.java)
            return response
        } else {
            val response: ResponseEntity<BaseResponse<AppVersionStatus>> = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse(
                    Status.ERROR, null, listOf(
                        Error("")
                    )))

            logger.printResponseInfo(response, ConfigController::class.java)
            return response
        }
    }
}
package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.AuthService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.auth.AuthToken
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @Autowired
    lateinit var logger: AppLogger

    @GetMapping("/get/{email}/{user-id}")
    fun validateUser(@PathVariable(value = "email") email: String, @PathVariable(value = "user-id") userId: String): ResponseEntity<BaseResponse<String>>?
    {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: validateUser:[/auth/get/$email/$userId]", AuthController::class.java)

        val response = authService.validateUser(email, userId)
        logger.printResponseInfo(response, AuthController::class.java)

        return response
    }

    @GetMapping("/get/token/{email}/{user-id}")
    fun requestToken(@PathVariable(value = "email") email: String, @PathVariable(value = "user-id") userId: String):
            ResponseEntity<BaseResponse<AuthToken>>? {

        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: requestToken:[/auth/get/token/$email/$userId]", AuthController::class.java)

        val response = authService.requestToken(email, userId)
        logger.printResponseInfo(response, AuthController::class.java)

        return response
    }

    @PostMapping("/create/token")
    fun newToken(@Valid @RequestBody authToken: AuthToken): ResponseEntity<BaseResponse<AuthToken>>?
    {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: newToken:[/auth/create/token]", AuthController::class.java)

        val response = authService.newToken(authToken)
        logger.printResponseInfo(response, AuthController::class.java)

        return response
    }

    @DeleteMapping("/delete/token/{token-id}")
    fun deleteToken(@PathVariable(value = "token-id") tokenId: String):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: deleteToken:[/auth/delete/token/$tokenId]", AuthController::class.java)

        val response = authService.deleteGivenToken(tokenId)
        logger.printResponseInfo(response, AuthController::class.java)

        return response
    }
}
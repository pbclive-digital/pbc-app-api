package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.UserService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.user.User
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @Autowired
    lateinit var logger: AppLogger

    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/user/create]", UserController::class.java)

        val response = userService.createUser(user)
        logger.printResponseInfo(response, UserController::class.java)

        return response
    }

    @GetMapping("/get/{user-id}")
    fun getUserById(@PathVariable(value = "user-id") userId: String):
            ResponseEntity<BaseResponse<User>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/user/get/$userId]", UserController::class.java)

        val response = userService.getUserById(userId)
        logger.printResponseInfo(response, UserController::class.java)

        return response
    }

    @GetMapping("/get/email/{email}")
    fun getUserByEmail(@PathVariable(value = "email") email: String):
            ResponseEntity<BaseResponse<User>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/user/get/email/$email]", UserController::class.java)

        val response = userService.getUserByEmail(email)
        logger.printResponseInfo(response, UserController::class.java)

        return response
    }

    @PutMapping("/update/{user-id}")
    fun updateUser(@PathVariable(value = "user-id") userId: String, @Valid @RequestBody user: User):
            ResponseEntity<BaseResponse<User>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: PUT: [/user/update/$userId]", UserController::class.java)

        val response = userService.updateUser(userId, user)
        logger.printResponseInfo(response, UserController::class.java)

        return response
    }
}
package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.AppointmentService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.appointment.Appointment
import com.kavi.pbc.live.data.model.appointment.AppointmentRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/appointment")
class AppointmentController(private val appointmentService: AppointmentService) {

    @Autowired
    lateinit var logger: AppLogger

    @PostMapping("/create")
    fun createNewAppointment(@Valid @RequestBody appointmentReq: AppointmentRequest,
                             @RequestHeader("X-app-user") userString: String?): ResponseEntity<BaseResponse<String>>? {
        println(userString)

        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/appointment/create]", AppointmentController::class.java)

        val response = appointmentService.createNewAppointment(appointmentReq = appointmentReq, userString = userString)
        logger.printResponseInfo(response, AppointmentController::class.java)

        return response
    }

    @GetMapping("/get/{user-id}")
    fun getUserAppointments(@PathVariable(value = "user-id") userId: String): ResponseEntity<BaseResponse<List<Appointment>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/appointment/get/$userId]", AppointmentController::class.java)

        val response = appointmentService.getUserAppointmentList(userId = userId)
        logger.printResponseInfo(response, AppointmentController::class.java)

        return response
    }
}
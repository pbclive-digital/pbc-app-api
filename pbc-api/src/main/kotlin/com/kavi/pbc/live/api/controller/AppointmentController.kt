package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.AppointmentService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.appointment.Appointment
import com.kavi.pbc.live.data.model.appointment.AppointmentRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
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
    fun createNewAppointment(@Valid @RequestBody appointment: Appointment): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/appointment/create]", AppointmentController::class.java)

        val response = appointmentService.createNewAppointment(appointment = appointment)
        logger.printResponseInfo(response, AppointmentController::class.java)

        return response
    }

    @PostMapping("/request/create")
    fun createNewAppointmentRequest(@Valid @RequestBody appointmentReq: AppointmentRequest): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/appointment/request/create]", AppointmentController::class.java)

        val response = appointmentService.createNewAppointmentRequest(appointmentReq = appointmentReq)
        logger.printResponseInfo(response, AppointmentController::class.java)

        return response
    }

    @GetMapping("/get/{user-id}")
    fun getUserAppointments(@PathVariable(value = "user-id") userId: String,
                            @RequestHeader("X-app-user") userString: String?): ResponseEntity<BaseResponse<List<Appointment>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/appointment/get/$userId]", AppointmentController::class.java)

        val response = appointmentService.getUserAppointmentList(userId = userId, userString = userString)
        logger.printResponseInfo(response, AppointmentController::class.java)

        return response
    }

    @GetMapping("/request/get/{user-id}")
    fun getUserAppointmentRequests(@PathVariable(value = "user-id") userId: String,
                            @RequestHeader("X-app-user") userString: String?): ResponseEntity<BaseResponse<List<AppointmentRequest>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/appointment/request/get/$userId]", AppointmentController::class.java)

        val response = appointmentService.getUserAppointmentRequestList(userId = userId, userString = userString)
        logger.printResponseInfo(response, AppointmentController::class.java)

        return response
    }

    @DeleteMapping("/delete/{appointment-id}")
    fun deleteAppointment(@PathVariable(value = "appointment-id") appointmentId: String):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: DELETE:[/appointment/delete/$appointmentId]", AppointmentController::class.java)

        val response = appointmentService.deleteAppointment(appointmentId)
        logger.printResponseInfo(response, AppointmentController::class.java)

        return response
    }

    @DeleteMapping("/request/delete/{appointment-id}")
    fun deleteAppointmentRequest(@PathVariable(value = "appointment-req-id") appointmentReqId: String):
            ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: DELETE:[/appointment/request/delete/$appointmentReqId]", AppointmentController::class.java)

        val response = appointmentService.deleteAppointmentRequest(appointmentReqId)
        logger.printResponseInfo(response, AppointmentController::class.java)

        return response
    }
}
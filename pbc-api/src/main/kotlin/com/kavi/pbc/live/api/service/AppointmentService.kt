package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.appointment.Appointment
import com.kavi.pbc.live.data.model.appointment.AppointmentRequest
import com.kavi.pbc.live.data.model.user.User
import com.kavi.pbc.live.data.model.user.UserType
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AppointmentService {

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun createNewAppointment(appointment: Appointment): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.createEntity(DatastoreConstant.APPOINTMENT_COLLECTION, appointment.id, appointment), null))
    }

    fun createNewAppointmentRequest(appointmentReq: AppointmentRequest): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract
                    .createEntity(DatastoreConstant.APPOINTMENT_REQUEST_COLLECTION, appointmentReq.id, appointmentReq), null))
    }

    fun getUserAppointmentList(userId: String, userString: String?): ResponseEntity<BaseResponse<List<Appointment>>> {

        val finalAppointmentList = if (userString != null && userString.isNotEmpty()) {
            val user = Json.decodeFromString<User>(userString)

            if (user.userType == UserType.MONK && user.residentMonk) {
                retrieveAppointmentForMonk(monkId = userId)
            } else {
                retrieveAppointmentForUser(userId)
            }
        } else {
            retrieveAppointmentForUser(userId)
        }

        return if (finalAppointmentList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, finalAppointmentList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    BaseResponse(
                        Status.ERROR, null, listOf(
                            Error(HttpStatus.NOT_FOUND.toString())
                        )
                    )
                )
        }
    }

    fun getUserAppointmentRequestList(userId: String, userString: String?): ResponseEntity<BaseResponse<List<AppointmentRequest>>> {

        val finalAppointmentRequestList = if (userString != null && userString.isNotEmpty()) {
            val user = Json.decodeFromString<User>(userString)

            if (user.userType == UserType.MONK && user.residentMonk) {
                retrieveAppointmentRequestsForMonk(monkId = userId)
            } else {
                retrieveAppointmentRequestsForUser(userId)
            }
        } else {
            retrieveAppointmentRequestsForUser(userId)
        }

        return if (finalAppointmentRequestList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, finalAppointmentRequestList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    BaseResponse(
                        Status.ERROR, null, listOf(
                            Error(HttpStatus.NOT_FOUND.toString())
                        )
                    )
                )
        }
    }

    fun deleteAppointment(appointmentId: String): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.deleteEntity(DatastoreConstant.APPOINTMENT_COLLECTION, appointmentId),
                null))
    }

    fun deleteAppointmentRequest(appointmentReqId: String): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.deleteEntity(DatastoreConstant.APPOINTMENT_REQUEST_COLLECTION, appointmentReqId),
                null))
    }

    private fun retrieveAppointmentForUser(userId: String): List<Appointment> {
        val properties = mapOf(
            "userId" to userId
        )

        val orderBy = mapOf(
            "property" to "date",
            "direction" to "ASC"
        )

        return datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.APPOINTMENT_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = Appointment::class.java
        )
    }

    private fun retrieveAppointmentForMonk(monkId: String): List<Appointment> {
        val properties = mapOf(
            "selectedMonkId" to listOf(monkId, "none")
        )

        val orderBy = mapOf(
            "property" to "date",
            "direction" to "ASC"
        )

        return datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.APPOINTMENT_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = Appointment::class.java
        )
    }

    private fun retrieveAppointmentRequestsForUser(userId: String): List<AppointmentRequest> {
        val properties = mapOf(
            "userId" to userId
        )

        return datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.APPOINTMENT_REQUEST_COLLECTION,
            propertiesMap = properties,
            className = AppointmentRequest::class.java
        )
    }

    private fun retrieveAppointmentRequestsForMonk(monkId: String): List<AppointmentRequest> {
        val properties = mapOf(
            "selectedMonkId" to listOf(monkId, "none")
        )

        return datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.APPOINTMENT_REQUEST_COLLECTION,
            propertiesMap = properties,
            className = AppointmentRequest::class.java
        )
    }
}
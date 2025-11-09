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
import com.kavi.pbc.live.data.util.DataUtil
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AppointmentService {

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun createNewAppointment(appointmentReq: AppointmentRequest, userString: String?): ResponseEntity<BaseResponse<String>> {
        userString?.let {
            val user = Json.decodeFromString<User>(it)

            val appointment = Appointment(
                id = DataUtil.idGenerator("apt"),
                userId = user.id,
                user = user,
                selectedMonk = appointmentReq.monk,
                dateAndTime = appointmentReq.dateAndTime,
                reason = appointmentReq.reasonForAppointment
            )

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse(Status.SUCCESS,
                    datastoreRepositoryContract.createEntity(DatastoreConstant.APPOINTMENT_COLLECTION, appointment.id, appointment), null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error("${HttpStatus.BAD_REQUEST.toString()} due to no valid user found on request"))
                ))
        }
    }

    fun getUserAppointmentList(userId: String): ResponseEntity<BaseResponse<List<Appointment>>> {
        val properties = mapOf(
            "userId" to userId
        )

        val orderBy = mapOf(
            "property" to "dateAndTime",
            "direction" to "ASC"
        )

        val userAppointmentList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.APPOINTMENT_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = Appointment::class.java
        )

        return if (userAppointmentList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, userAppointmentList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }
}
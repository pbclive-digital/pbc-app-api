package com.kavi.pbc.live.data.model.appointment

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.user.User

data class AppointmentRequest(
    val monk: User? = null,
    val dateAndTime: Long,
    val reasonForAppointment: String
): BaseModel {

    constructor(): this(null,0, "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "monk" to monk,
        "dateAndTime" to dateAndTime,
        "reasonForAppointment" to reasonForAppointment
    )

}

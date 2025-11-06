package com.kavi.pbc.live.data.model.appointment

import com.kavi.pbc.live.data.model.BaseModel

data class AppointmentRequest(
    val dateAndTime: Long,
    val reasonForAppointment: String
): BaseModel {

    constructor(): this(0, "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "dateAndTime" to dateAndTime,
        "reasonForAppointment" to reasonForAppointment
    )

}

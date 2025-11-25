package com.kavi.pbc.live.data.model.appointment

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.user.User

data class AppointmentRequest(
    val title: String,
    val monk: User? = null,
    val date: Long,
    val time: String,
    val reasonForAppointment: String
): BaseModel {

    constructor(): this("", null,0, "", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "title" to title,
        "monk" to monk,
        "date" to date,
        "time" to time,
        "reasonForAppointment" to reasonForAppointment
    )

}

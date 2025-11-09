package com.kavi.pbc.live.data.model.appointment

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.user.User
import com.kavi.pbc.live.data.util.DataUtil

data class Appointment(
    val id: String,
    val userId: String,
    val user: User,
    val selectedMonk: User? = null,
    val dateAndTime: Long,
    val reason: String,
    val appointmentStatus: AppointmentStatus = AppointmentStatus.PENDING
): BaseModel {

    constructor(): this(DataUtil.idGenerator("apt"), "", User(), null,
        0, "", AppointmentStatus.PENDING)

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "userId" to userId,
        "user" to user,
        "selectedMonk" to selectedMonk,
        "dateAndTime" to dateAndTime,
        "reason" to reason,
        "appointmentStatus" to appointmentStatus
    )

}

package com.kavi.pbc.live.data.model.appointment

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.user.User
import com.kavi.pbc.live.data.util.DataUtil

data class AppointmentRequest(
    val id: String,
    val title: String = "",
    val userId: String = "",
    val user: User,
    var selectedMonkId: String = "",
    var selectedMonk: User? = null,
    var reason: String = "",
    var appointmentReqType: AppointmentRequestType = AppointmentRequestType.REMOTE
): BaseModel {

    constructor(): this(DataUtil.idGenerator("aptr"), "", "", User(),
        "none", null, "", AppointmentRequestType.REMOTE
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "title" to title,
        "userId" to userId,
        "user" to user,
        "selectedMonkId" to selectedMonkId,
        "selectedMonk" to selectedMonk,
        "reason" to reason,
        "appointmentReqType" to appointmentReqType
    )
}

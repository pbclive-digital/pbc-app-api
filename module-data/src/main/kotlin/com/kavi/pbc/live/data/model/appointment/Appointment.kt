package com.kavi.pbc.live.data.model.appointment

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.user.User
import com.kavi.pbc.live.data.util.DataUtil

data class Appointment(
    val id: String,
    val userId: String,
    val user: User,
    val dateAndTime: Long,
    val reason: String
): BaseModel {

    constructor(): this(DataUtil.idGenerator("apt"), "", User(), 0, "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "userId" to userId,
        "user" to user,
        "dateAndTime" to dateAndTime,
        "reason" to reason
    )

}

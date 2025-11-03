package com.kavi.pbc.live.data.model.event.register

import com.kavi.pbc.live.data.model.BaseModel

data class EventRegistrationItem(
    val participantUserId: String,
    val participantName: String,
    val participantContactNumber: String? = null,
    val participantAddress: String? = null
): BaseModel {

    constructor(): this(
        participantUserId = "", "", null, null
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "participantUserId" to participantUserId,
        "participantName" to participantName,
        "participantContactNumber" to participantContactNumber,
        "participantAddress" to participantAddress
    )
}
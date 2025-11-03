package com.kavi.pbc.live.data.model.event.register

import com.kavi.pbc.live.data.model.BaseModel

data class EventRegistration(
    val id: String,
    val availableSeatCount: Int,
    val registrationList: MutableList<EventRegistrationItem> = mutableListOf()
): BaseModel {

    constructor(): this(id = "", availableSeatCount = 0, registrationList = mutableListOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "availableSeatCount" to availableSeatCount,
        "registrationList" to registrationList
    )
}

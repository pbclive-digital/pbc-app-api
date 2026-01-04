package com.kavi.pbc.live.data.model.event.potluck

import com.kavi.pbc.live.data.model.BaseModel

data class EventPotluck(
    val id: String,
    val potluckItemList: MutableList<EventPotluckItem> = mutableListOf()
): BaseModel {

    constructor(): this("", mutableListOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "potluckItemList" to potluckItemList
    )
}

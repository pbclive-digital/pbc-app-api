package com.kavi.pbc.live.data.model.event.potluck

import com.kavi.pbc.live.data.model.BaseModel

data class EventPotluckItem(
    val itemId: String,
    val itemName: String,
    val availableCount: Int,
    val contributorList: MutableList<EventPotluckContributor> = mutableListOf()
): BaseModel {

    constructor(): this("","", 0, mutableListOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "itemId" to itemId,
        "itemName" to itemName,
        "availableCount" to availableCount,
        "contributorList" to contributorList
    )
}

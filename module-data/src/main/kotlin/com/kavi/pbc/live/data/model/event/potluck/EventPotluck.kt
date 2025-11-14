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

data class EventPotluckContributor(
    val contributorId: String,
    val contributorName: String,
    val contributorContactNumber: String
): BaseModel {

    constructor(): this("", "", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "contributorId" to contributorId,
        "contributorName" to contributorName,
        "contributorContactNumber" to contributorContactNumber
    )
}

package com.kavi.pbc.live.data.model.event.potluck

import com.kavi.pbc.live.data.model.BaseModel

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

package com.kavi.pbc.live.data.model.event.signup.sheet

import com.kavi.pbc.live.data.model.BaseModel

data class EventSignUpSheetContributor(
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

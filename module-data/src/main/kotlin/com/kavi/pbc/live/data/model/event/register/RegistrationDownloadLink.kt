package com.kavi.pbc.live.data.model.event.register

import com.kavi.pbc.live.data.model.BaseModel

data class RegistrationDownloadLink(
    val eventId: String?,
    val downloadLink: String?
): BaseModel {
    constructor(): this(null, null)

    override fun toMap(): Map<String, Any?> = mapOf(
        "eventId" to eventId,
        "downloadLink" to downloadLink
    )
}
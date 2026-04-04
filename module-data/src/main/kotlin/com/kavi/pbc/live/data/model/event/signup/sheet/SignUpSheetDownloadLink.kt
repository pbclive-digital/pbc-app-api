package com.kavi.pbc.live.data.model.event.signup.sheet

import com.kavi.pbc.live.data.model.BaseModel

data class SignUpSheetDownloadLink(
    val eventId: String?,
    val sheetId: String?,
    val downloadLink: String?
): BaseModel {
    constructor(): this(null, null, null)

    override fun toMap(): Map<String, Any?> = mapOf(
        "eventId" to eventId,
        "sheetId" to sheetId,
        "downloadLink" to downloadLink
    )
}
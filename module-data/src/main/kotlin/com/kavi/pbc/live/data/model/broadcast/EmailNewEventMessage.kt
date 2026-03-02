package com.kavi.pbc.live.data.model.broadcast

import com.kavi.pbc.live.data.model.BaseModel

data class EmailNewEventMessage(
    val subject: String,
    val title: String,
    val message: String,
    val eventDescription: String,
    val eventUrl: String,
): BaseModel {
    constructor(): this("", "", "", "", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "subject" to subject,
        "title" to title,
        "message" to message,
        "eventDescription" to eventDescription,
        "eventUrl" to eventUrl,
    )
}

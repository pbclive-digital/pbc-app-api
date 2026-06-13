package com.kavi.pbc.live.data.model.broadcast.record

import com.kavi.pbc.live.data.model.BaseModel

data class EmailRecordContent(
    val subject: String = "",
    val title: String = "",
    val message: String = "",
    val eventDescription: String? = null,
    val eventAgenda: List<String> = emptyList(),
    val eventUrl: String? = null,
): BaseModel {

    constructor(): this("", "", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "subject" to subject,
        "title" to title,
        "message" to message,
        "eventDescription" to eventDescription,
        "eventAgenda" to eventAgenda,
        "eventUrl" to eventUrl
    )
}

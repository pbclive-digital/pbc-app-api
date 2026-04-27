package com.kavi.pbc.live.api.data.dto.request

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.email.EmailGroupHeading
import com.kavi.pbc.live.data.model.event.Event

data class PublishEventRequest(
    val event: Event,
    val emailGroupHeadings: List<EmailGroupHeading>
): BaseModel {

    constructor() : this(Event(), listOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "event" to event,
        "emailGroupHeadings" to emailGroupHeadings
    )
}

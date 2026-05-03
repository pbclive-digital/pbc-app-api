package com.kavi.pbc.live.api.data.dto.request

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.broadcast.EmailBroadcastMessage
import com.kavi.pbc.live.data.model.email.EmailGroupHeading

data class EmailBroadcastRequest(
    val emailBroadcastMessage: EmailBroadcastMessage,
    val emailGroupHeadings: List<EmailGroupHeading>
): BaseModel {

    constructor() : this(EmailBroadcastMessage(), listOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "emailBroadcastMessage" to emailBroadcastMessage,
        "emailGroupHeadings" to emailGroupHeadings
    )
}

package com.kavi.pbc.live.data.model.broadcast

import com.kavi.pbc.live.data.model.BaseModel

data class BroadcastMessage(
    val title: String,
    val message: String
): BaseModel {

    constructor(): this("", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "title" to title,
        "message" to message
    )
}

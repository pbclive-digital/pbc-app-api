package com.kavi.pbc.live.data.model.notification

import com.kavi.pbc.live.data.model.BaseModel

data class PushTokenData(
    val email: String,
    val pushToken: String
): BaseModel {
    constructor(): this("", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "email" to email,
        "pushToken" to pushToken
    )
}
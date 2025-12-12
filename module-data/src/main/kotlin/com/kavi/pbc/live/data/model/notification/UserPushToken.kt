package com.kavi.pbc.live.data.model.notification

import com.kavi.pbc.live.data.model.BaseModel

data class UserPushToken(
    val userId: String,
    val email: String,
    val pushTokenList: MutableList<String>
): BaseModel {
    constructor(): this("", "", mutableListOf())
    override fun toMap(): Map<String, Any?> = mapOf(
        "userId" to userId,
        "email" to email,
        "pushTokenList" to pushTokenList
    )

}
package com.kavi.pbc.live.data.model.user

import com.kavi.pbc.live.data.model.BaseModel

data class UserSummary(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = ""
): BaseModel {

    constructor(): this("", "", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "imageUrl" to imageUrl
    )
}
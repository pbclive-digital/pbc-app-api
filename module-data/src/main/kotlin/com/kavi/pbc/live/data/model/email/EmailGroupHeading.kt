package com.kavi.pbc.live.data.model.email

import com.kavi.pbc.live.data.model.BaseModel

data class EmailGroupHeading(
    val id: String,
    val name: String,
): BaseModel {

    constructor(): this("", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
    )
}
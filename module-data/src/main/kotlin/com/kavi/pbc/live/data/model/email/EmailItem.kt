package com.kavi.pbc.live.data.model.email

import com.kavi.pbc.live.data.model.BaseModel

data class EmailItem(
    val email: String,
    val ownerName: String? = null,
): BaseModel {

    constructor(): this("", "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "email" to this.email,
        "ownerName" to this.ownerName,
    )
}

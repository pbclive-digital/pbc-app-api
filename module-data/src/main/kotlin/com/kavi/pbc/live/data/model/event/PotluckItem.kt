package com.kavi.pbc.live.data.model.event

import com.kavi.pbc.live.data.model.BaseModel

data class PotluckItem(
    val itemName: String,
    val itemCount: Int
): BaseModel {
    constructor(): this("", 0)

    override fun toMap(): Map<String, Any?> = mapOf(
        "itemName" to itemName,
        "itemCount" to itemCount
    )

}

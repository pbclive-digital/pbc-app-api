package com.kavi.pbc.live.data.model.event.potluck

import com.kavi.pbc.live.data.model.BaseModel

data class ContributionItem(
    val itemId: String,
    val itemName: String,
    val itemCount: Int
): BaseModel {
    constructor(): this("", "", 0)

    override fun toMap(): Map<String, Any?> = mapOf(
        "itemId" to itemId,
        "itemName" to itemName,
        "itemCount" to itemCount
    )

}
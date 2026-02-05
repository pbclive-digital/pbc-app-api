package com.kavi.pbc.live.data.model.event.signup.sheet

import com.kavi.pbc.live.data.model.BaseModel

data class SignUpSheet(
    val sheetId: String,
    val sheetName: String,
    val sheetDescription: String,
    val availableCount: Int,
    val allowMultiSignUps: Boolean,
): BaseModel {
    constructor(): this("", "", "", 0, false)

    override fun toMap(): Map<String, Any?> = mapOf(
        "sheetId" to sheetId,
        "sheetName" to sheetName,
        "sheetDescription" to sheetDescription,
        "availableCount" to availableCount,
        "allowMultiSignUps" to allowMultiSignUps
    )
}
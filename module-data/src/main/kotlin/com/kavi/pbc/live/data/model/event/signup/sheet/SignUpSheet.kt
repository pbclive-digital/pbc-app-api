package com.kavi.pbc.live.data.model.event.signup.sheet

import com.kavi.pbc.live.data.model.BaseModel

data class SignUpSheet(
    val sheetName: String,
    val signUpAvailabilityCount: Int
): BaseModel {
    constructor(): this("", 0)

    override fun toMap(): Map<String, Any?> = mapOf(
        "sheetName" to sheetName,
        "signUpAvailabilityCount" to signUpAvailabilityCount
    )
}
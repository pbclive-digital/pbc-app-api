package com.kavi.pbc.live.data.model.appointment

import com.kavi.pbc.live.data.model.BaseModel

data class AppointmentCountValidate(
    val acceptedCount: Int,
    val requestCount: Int,
    val allowToCreateRequest: Boolean
): BaseModel {

    constructor(): this(0, 0, false)

    override fun toMap(): Map<String, Any?> = mapOf(
        "acceptedCount" to acceptedCount,
        "requestCount" to requestCount,
        "allowToCreateRequest" to allowToCreateRequest
    )
}

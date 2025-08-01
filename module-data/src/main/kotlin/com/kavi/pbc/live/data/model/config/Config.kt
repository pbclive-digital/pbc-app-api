package com.kavi.pbc.live.data.model.config

import com.kavi.pbc.live.data.DataConstant
import com.kavi.pbc.live.data.model.BaseModel

data class Config(
    var dashboardEventCount: Int
): BaseModel {
    constructor(): this(
        DataConstant.DASHBOARD_EVENT_COUNT
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "dashboardEventCount" to dashboardEventCount
    )
}
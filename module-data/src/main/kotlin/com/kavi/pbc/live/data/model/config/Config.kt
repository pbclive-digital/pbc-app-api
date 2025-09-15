package com.kavi.pbc.live.data.model.config

import com.kavi.pbc.live.data.DataConstant
import com.kavi.pbc.live.data.model.BaseModel

data class Config(
    var dashboardEventCount: Int,
    var dailyQuotesCount: Int
): BaseModel {
    constructor(): this(
        DataConstant.DASHBOARD_EVENT_COUNT,
        DataConstant.DAILY_QUOTE_COUNT
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "dashboardEventCount" to dashboardEventCount,
        "dailyQuotesCount" to dailyQuotesCount
    )
}
package com.kavi.pbc.live.data.model.config

import com.kavi.pbc.live.data.model.BaseModel

data class AppVersionStatus(
    val support: Boolean,
    val supportingVersion: String
): BaseModel {
    constructor(): this(false, "")

    override fun toMap(): Map<String, Any?> = mapOf(
        "support" to support,
        "supportingVersion" to supportingVersion
    )
}

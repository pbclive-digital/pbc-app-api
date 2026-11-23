package com.kavi.pbc.live.data.model.privacy

import com.kavi.pbc.live.data.model.BaseModel

data class PrivacyPolicy(
    val lastUpdated: String,
    val policyItemList: List<PolicyItem>
): BaseModel {
    constructor(): this("", listOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "lastUpdated" to lastUpdated,
        "policyItemList" to policyItemList
    )
}

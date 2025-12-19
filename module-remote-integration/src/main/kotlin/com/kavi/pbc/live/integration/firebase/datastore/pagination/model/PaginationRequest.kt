package com.kavi.pbc.live.integration.firebase.datastore.pagination.model

import com.kavi.pbc.live.data.model.BaseModel

data class PaginationRequest(
    val previousPageLastDocKey: String?
): BaseModel {
    constructor(): this(null)

    override fun toMap(): Map<String, Any?> = mapOf(
        "previousPageLastDocKey" to previousPageLastDocKey
    )
}

package com.kavi.pbc.live.integration.firebase.datastore.pagination.model

import com.kavi.pbc.live.data.model.BaseModel

data class PaginationResponse<T>(
    val entityList: List<T>,
    val previousPageLastDocKey: String?
): BaseModel {
    constructor(): this(listOf(), null)

    override fun toMap(): Map<String, Any?> = mapOf(
        "entityList" to entityList,
        "previousPageLastDocKey" to previousPageLastDocKey
    )
}

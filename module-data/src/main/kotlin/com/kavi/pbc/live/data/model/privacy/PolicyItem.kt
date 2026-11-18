package com.kavi.pbc.live.data.model.privacy

import com.kavi.pbc.live.data.model.BaseModel

data class PolicyItem(
    val id: String,
    val title: String,
    val description: String,
    val secondDescription: String?,
    val pointList: List<String>
): BaseModel {
    constructor() : this("","", "", "", listOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "title" to title,
        "description" to description,
        "secondDescription" to secondDescription,
        "pointList" to pointList
    )

}

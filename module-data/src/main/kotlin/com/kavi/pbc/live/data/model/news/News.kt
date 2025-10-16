package com.kavi.pbc.live.data.model.news

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.util.DataUtil

data class News(
    val id: String,
    val title: String,
    val content: String,
    val newsStatus: NewsStatus = NewsStatus.DRAFT,
    val facebookLink: String? = null,
    val createdTime: Long
): BaseModel {
    constructor(): this(
        DataUtil.idGenerator("nsw"), "", "", NewsStatus.DRAFT, null, System.currentTimeMillis()
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "title" to title,
        "content" to content,
        "newsStatus" to newsStatus,
        "facebookLink" to facebookLink,
        "createdTime" to createdTime
    )
}
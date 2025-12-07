package com.kavi.pbc.live.data.model.news

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.util.DataUtil

data class News(
    val id: String,
    val title: String,
    val content: String,
    var newsStatus: NewsStatus = NewsStatus.DRAFT,
    val facebookLink: String? = null,
    val newsImage: String? = null,
    val createdTime: Long,
    var publishedTime: Long = 0
): BaseModel {
    constructor(): this(
        DataUtil.idGenerator("nsw"), "", "", NewsStatus.DRAFT, null, null,
        System.currentTimeMillis(), 0
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "title" to title,
        "content" to content,
        "newsStatus" to newsStatus,
        "facebookLink" to facebookLink,
        "newsImage" to newsImage,
        "createdTime" to createdTime,
        "publishedTime" to publishedTime
    )
}
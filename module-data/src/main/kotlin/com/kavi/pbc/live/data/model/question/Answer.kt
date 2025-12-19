package com.kavi.pbc.live.data.model.question

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.user.UserSummary

data class Answer(
    val comment: String,
    val createdTime: Long,
    val author: UserSummary
): BaseModel {

    constructor(): this("", System.currentTimeMillis(), UserSummary())

    override fun toMap(): Map<String, Any?> = mapOf(
        "comment" to comment,
        "createdTime" to createdTime,
        "author" to author
    )
}

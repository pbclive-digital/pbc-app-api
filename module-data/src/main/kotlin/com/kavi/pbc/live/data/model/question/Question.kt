package com.kavi.pbc.live.data.model.question

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.user.User
import com.kavi.pbc.live.data.util.DataUtil

data class Question(
    val id: String,
    val title: String,
    val content: String,
    val createdTime: Long,
    val authorId: String,
    val author: User,
    val answerList: MutableList<Answer> = mutableListOf()
): BaseModel {

    constructor(): this(DataUtil.idGenerator("que"), "", "",
        System.currentTimeMillis(), "", User())

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "title" to title,
        "content" to content,
        "createdTime" to createdTime,
        "authorId" to authorId,
        "author" to author,
        "answerList" to answerList
    )
}

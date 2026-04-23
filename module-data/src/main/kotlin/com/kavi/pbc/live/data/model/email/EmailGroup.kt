package com.kavi.pbc.live.data.model.email

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.util.DataUtil

data class EmailGroup(
    val id: String,
    val name: String,
    var emails: MutableList<EmailItem> = mutableListOf(),
): BaseModel {

    constructor() : this(DataUtil.idGenerator("eml"), "", mutableListOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "emails" to emails
    )
}

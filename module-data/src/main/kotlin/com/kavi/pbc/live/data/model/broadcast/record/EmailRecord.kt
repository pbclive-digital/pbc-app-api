package com.kavi.pbc.live.data.model.broadcast.record

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.broadcast.EmailTemplateType
import com.kavi.pbc.live.data.model.email.EmailGroupHeading
import com.kavi.pbc.live.data.util.DataUtil

data class EmailRecord(
    val id: String = DataUtil.idGenerator("bcr"),
    val emailTemplate: EmailTemplateType,
    val sentTime: Long = System.currentTimeMillis(),
    val emailGroupHeadings: List<EmailGroupHeading> = listOf(),
    val emailRecordContent: EmailRecordContent
): BaseModel {

    constructor(): this(
        DataUtil.idGenerator("bcr"),
        EmailTemplateType.BROADCAST,
        System.currentTimeMillis(),
        emptyList<EmailGroupHeading>(),
        EmailRecordContent()
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "emailTemplate" to emailTemplate,
        "sentTime" to sentTime,
        "emailGroupHeadings" to emailGroupHeadings,
        "emailRecordContent" to emailRecordContent
    )
}
package com.kavi.pbc.live.data.model.event.signup.sheet

import com.kavi.pbc.live.data.model.BaseModel

data class EventSignUpSheet (
    val sheetId: String,
    val sheetName: String,
    val sheetDescription: String,
    val availableCount: Int,
    val contributorList: MutableList<EventSignUpSheetContributor> = mutableListOf()
): BaseModel {

    constructor(): this("","", "",0, mutableListOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "sheetId" to sheetId,
        "sheetName" to sheetName,
        "sheetDescription" to sheetDescription,
        "availableCount" to availableCount,
        "contributorList" to contributorList
    )
}
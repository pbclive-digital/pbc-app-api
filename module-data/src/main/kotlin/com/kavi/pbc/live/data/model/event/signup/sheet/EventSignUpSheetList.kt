package com.kavi.pbc.live.data.model.event.signup.sheet

import com.kavi.pbc.live.data.model.BaseModel

data class EventSignUpSheetList (
    val id: String,
    val signUpSheetItemList: MutableList<EventSignUpSheet> = mutableListOf()
): BaseModel {
    constructor(): this("", mutableListOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "signUpSheetItemList" to signUpSheetItemList
    )
}
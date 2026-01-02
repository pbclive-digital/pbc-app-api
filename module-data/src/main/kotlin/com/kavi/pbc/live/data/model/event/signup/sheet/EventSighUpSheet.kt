package com.kavi.pbc.live.data.model.event.signup.sheet

import com.kavi.pbc.live.data.model.BaseModel

data class EventSighUpSheet (
    val id: String,
    val signUpSheetItemList: MutableList<SignUpSheetItem> = mutableListOf()
): BaseModel {
    constructor(): this("", mutableListOf())

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "signUpSheetItemList" to signUpSheetItemList
    )
}
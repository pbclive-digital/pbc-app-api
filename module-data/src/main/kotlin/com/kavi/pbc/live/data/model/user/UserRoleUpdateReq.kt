package com.kavi.pbc.live.data.model.user

import com.kavi.pbc.live.data.model.BaseModel

data class UserRoleUpdateReq(
    val newRole: String = "",
    val residentMonkFlag: Boolean = false,
    val user: User
): BaseModel {

    constructor(): this("", false, User())

    override fun toMap(): Map<String, Any?> = mapOf(
        "newRole" to newRole,
        "residentMonkFlag" to residentMonkFlag,
        "user" to user
    )
}
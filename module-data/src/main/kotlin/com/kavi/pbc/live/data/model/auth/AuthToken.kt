package com.kavi.pbc.live.data.model.auth

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.util.DataUtil

data class AuthToken(
    var id: String?,
    val email: String,
    val userId: String,
    var token: String?,
    val status: AuthTokenStatus,
    val createdTime: Long?,
    var lastUsedAt: Long?
): BaseModel {
    constructor(): this(DataUtil.idGenerator("tkn"), "", "", null, AuthTokenStatus.REMOVED, System.currentTimeMillis(), null)

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "email" to email,
        "userId" to userId,
        "token" to token,
        "status" to status,
        "createdTime" to createdTime,
        "lastUsedAt" to lastUsedAt
    )
}
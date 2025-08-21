package com.kavi.pbc.live.data.model.user

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.util.DataUtil

data class User(
    val id: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String? = null,
    val profilePicUrl: String? = null,
    val address: String? = null,
    val userType: UserType = UserType.CONSUMER,
    val userAuthType: UserAuthType? = UserAuthType.NONE,
): BaseModel {
    constructor(): this(
        DataUtil.idGenerator("usr"), "", null, null, null,
        null, null, UserType.CONSUMER, UserAuthType.NONE)

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "email" to email,
        "firstName" to firstName,
        "lastName" to lastName,
        "phoneNum" to phoneNumber,
        "profilePicUrl" to profilePicUrl,
        "address" to address,
        "userType" to userType,
        "userAuthType" to userAuthType
    )
}

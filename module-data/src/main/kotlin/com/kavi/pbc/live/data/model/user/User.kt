package com.kavi.pbc.live.data.model.user

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.util.DataUtil
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val firstName: String?,
    var uppercaseFirstName: String?,
    val lastName: String?,
    var uppercaseLastName: String?,
    val phoneNumber: String? = null,
    val profilePicUrl: String? = null,
    val address: String? = null,
    var userType: UserType = UserType.CONSUMER,
    val userAuthType: UserAuthType? = UserAuthType.NONE,
): BaseModel {
    constructor(): this(
        DataUtil.idGenerator("usr"), "", null, null,null, null,null,
        null, null, UserType.CONSUMER, UserAuthType.NONE)

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "email" to email,
        "firstName" to firstName,
        "uppercaseFirstName" to uppercaseFirstName,
        "lastName" to lastName,
        "uppercaseLastName" to uppercaseLastName,
        "phoneNumber" to phoneNumber,
        "profilePicUrl" to profilePicUrl,
        "address" to address,
        "userType" to userType,
        "userAuthType" to userAuthType
    )
}

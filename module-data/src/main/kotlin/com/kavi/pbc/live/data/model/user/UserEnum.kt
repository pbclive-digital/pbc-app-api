package com.kavi.pbc.live.data.model.user

enum class UserType(val type: Int) {
    ADMIN(101),
    THERO(102),
    CONSUMER(103)
}

enum class UserAuthType(val authType: Int) {
    GOOGLE(201),
    APPLE(202),
    NONE(-200)
}
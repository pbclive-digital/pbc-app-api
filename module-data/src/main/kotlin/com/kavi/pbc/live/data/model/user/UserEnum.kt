package com.kavi.pbc.live.data.model.user

enum class UserType(val type: Int) {
    ADMIN(101),
    MONK(102),
    MANAGER(103),
    CONSUMER(104)
}

enum class UserAuthType(val authType: Int) {
    GOOGLE(201),
    APPLE(202),
    NONE(-200)
}
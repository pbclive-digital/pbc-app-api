package com.kavi.droid.survey.api.dto

enum class Status {
    SUCCESS, ERROR
}

data class BaseResponse<T>(
    val status: Status,
    val body: T?,
    val errors: List<Error>?
)

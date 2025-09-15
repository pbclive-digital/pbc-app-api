package com.kavi.pbc.live.data.model

data class DailyQuote (
    val quote: String,
    val author: String
): BaseModel {
    constructor(): this (
        "", ""
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "quote" to quote,
        "author" to author
    )
}

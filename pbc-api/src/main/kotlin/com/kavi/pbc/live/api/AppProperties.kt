package com.kavi.pbc.live.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {
    @Value("\${app.version}")
    lateinit var appVersion: String

    @Value("\${app.env}")
    lateinit var appEnv: String
}
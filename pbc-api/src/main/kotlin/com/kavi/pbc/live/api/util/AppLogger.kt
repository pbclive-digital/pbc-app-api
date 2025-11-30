package com.kavi.pbc.live.api.util

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class AppLogger {

    private val kLogger = KotlinLogging.logger {}

    fun printSeparator() {
        kLogger.info { System.lineSeparator() }
    }

    fun <T>printInfo(message: String, javaClass: Class<T>) {
        kLogger.info { "${javaClass.name}: $message" }
    }

    fun <T : Any, E>printResponseInfo(response: ResponseEntity<T>?, javaClass: Class<E>) {
        kLogger.info { "${javaClass.name}: RESPONSE CODE: ${response?.statusCode}" }
        kLogger.info { "${javaClass.name}: RESPONSE BODY: ${response?.body}" }
    }
}
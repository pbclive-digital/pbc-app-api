package com.kavi.pbc.live.api.config

import com.kavi.pbc.live.api.AppProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig: WebMvcConfigurer {

    @Autowired
    lateinit var appProperties: AppProperties

    override fun addCorsMappings(registry: CorsRegistry) {
        val allowedOrigins = when(appProperties.appEnv) {
            "dev", "staging" -> "http://localhost:8080,https://pbclive-digital.github.io/"
            else -> ""
        }

        registry.addMapping("/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
    }
}
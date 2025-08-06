package com.kavi.pbc.live.api.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity, tokenFilter: AppTokenFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/event/create/**").authenticated()
                    .requestMatchers("/user/update/**").authenticated()
                    .anyRequest().permitAll()
            }
            .addFilterBefore(tokenFilter, BasicAuthenticationFilter::class.java)
            .httpBasic { it.disable() }

        return http.build()
    }

    @Bean
    fun tokenFilter(): AppTokenFilter {
        return AppTokenFilter()
    }
}
package com.kavi.pbc.live.api.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.firewall.StrictHttpFirewall

@Configuration
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity, tokenFilter: AppTokenFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/event/get/draft/**").authenticated()
                    .requestMatchers("/event/create/**").authenticated()
                    .requestMatchers("/event/add/image/**").authenticated()
                    .requestMatchers("/event/register/**").authenticated()
                    .requestMatchers("/event/unregister/**").authenticated()
                    .requestMatchers("/event/put/publish/**").authenticated()
                    .requestMatchers("/event/update/**").authenticated()
                    .requestMatchers("/event/delete/**").authenticated()
                    .requestMatchers("/user/update/**").authenticated()
                    .requestMatchers("/appointment/**").authenticated()
                    .requestMatchers("/news/create/**").authenticated()
                    .requestMatchers("/news/update/**").authenticated()
                    .requestMatchers("/news/get/draft/**").authenticated()
                    .requestMatchers("/news/update/publish/**").authenticated()
                    .requestMatchers("/news/delete/**").authenticated()
                    .requestMatchers("/news/add/image/**").authenticated()
                    .requestMatchers("/broadcast/message/**").authenticated()
                    .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
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
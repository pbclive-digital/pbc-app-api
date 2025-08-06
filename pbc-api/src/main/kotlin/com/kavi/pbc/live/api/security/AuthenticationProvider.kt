package com.kavi.pbc.live.api.security

import com.kavi.pbc.live.api.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class AuthenticationProvider: AbstractUserDetailsAuthenticationProvider() {

    @Autowired
    lateinit var authService: AuthService

    override fun additionalAuthenticationChecks(userDetails: UserDetails?,
        authentication: UsernamePasswordAuthenticationToken?) {}

    override fun retrieveUser(username: String?,
                              authentication: UsernamePasswordAuthenticationToken?): UserDetails {
        val token = authentication?.credentials
        return authService.findByToken(token.toString())
    }
}
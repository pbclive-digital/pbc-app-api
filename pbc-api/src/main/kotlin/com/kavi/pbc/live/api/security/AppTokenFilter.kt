package com.kavi.pbc.live.api.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class AppTokenFilter: OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Extract token from Authorization header
        val authHeader = request.getHeader(AUTHORIZATION)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7) // Extract token part
            // In a real application, validate the token (e.g., using JWT parsing logic)

            val requestAuthentication: Authentication = UsernamePasswordAuthenticationToken(token, token, listOf())
            SecurityContextHolder.getContext().authentication = requestAuthentication
        }

        filterChain.doFilter(request, response)  // Continue the request processing
    }
}
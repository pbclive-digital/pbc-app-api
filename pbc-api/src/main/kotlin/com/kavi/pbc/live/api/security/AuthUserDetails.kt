package com.kavi.pbc.live.api.security

import com.kavi.pbc.live.data.model.auth.AuthToken
import com.kavi.pbc.live.data.model.auth.AuthTokenStatus
import com.kavi.pbc.live.data.model.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthUserDetails(private val user: User?, private val authToken: AuthToken?): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String? {
        user?.let {
            return authToken?.token
        }?: run {
            return null
        }
    }

    override fun getUsername(): String? {
        user?.let {
            return it.email
        }?: run {
            return null
        }
    }

    override fun isAccountNonExpired(): Boolean {
        return authToken?.let {
            when(it.status) {
                AuthTokenStatus.VALID -> true
                else -> false
            }
        }?: run {
            false
        }
    }

    override fun isAccountNonLocked(): Boolean {
        return user?.let {
            true
        }?: run {
            false
        }
    }

    override fun isCredentialsNonExpired(): Boolean {
        return authToken?.let {
            when(it.status) {
                AuthTokenStatus.VALID -> true
                else -> false
            }
        }?: run {
            false
        }
    }

    override fun isEnabled(): Boolean {
        return user?.let {
            true
        }?: run {
            false
        }
    }
}
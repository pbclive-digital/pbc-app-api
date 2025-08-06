package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.auth.UserAuth
import com.kavi.pbc.live.data.model.auth.AuthToken
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthService {

    val userAuth = UserAuth()

    fun validateUser(email: String, userId: String): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS, userAuth.userStatus(email, userId), null))
    }

    fun requestToken(email: String, userId: String): ResponseEntity<BaseResponse<AuthToken>> {
        userAuth.requestToken(email, userId)?.let {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse(Status.SUCCESS, it, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(message = HttpStatus.NOT_FOUND.toString())
                )))
        }
    }

    fun newToken(authToken: AuthToken): ResponseEntity<BaseResponse<AuthToken>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS, userAuth.generateAuthToken(authToken), null))
    }

    fun deleteGivenToken(tokenId: String): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS, userAuth.removeGivenToken(tokenId), null))
    }
}
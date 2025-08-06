package com.kavi.pbc.live.auth

import com.kavi.pbc.live.data.model.auth.AuthToken
import java.util.Base64

class TokenGenerator {

    fun generateToken(authToken: AuthToken): String {
        return "${encoder("SUR")}T${encoder(authToken.userId)}I" +
                "${encoder(System.currentTimeMillis().toString())}M${encoder(authToken.email)}" +
                "E${encoder("VEY")}"
    }

    private fun encoder(encodingString: String): String {
        // encode a string using Base64 encoder
        val encoder: Base64.Encoder = Base64.getEncoder()
        return encoder.encodeToString(encodingString.toByteArray())
    }
}
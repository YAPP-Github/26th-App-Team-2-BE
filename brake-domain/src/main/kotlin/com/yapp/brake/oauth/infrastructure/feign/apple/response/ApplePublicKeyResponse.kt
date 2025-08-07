package com.yapp.brake.oauth.infrastructure.feign.apple.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode

data class ApplePublicKeyResponse(val keys: List<ApplePublicKey>) {
    fun getMatchedKey(kid: String): ApplePublicKey {
        return keys
            .firstOrNull { key -> key.kid == kid && key.algorithm == "RS256" }
            ?: throw CustomException(ErrorCode.UNAUTHORIZED)
    }
}

data class ApplePublicKey(
    @JsonProperty("kid")
    val kid: String,
    @JsonProperty("alg")
    val algorithm: String,
    @JsonProperty("use")
    val use: String,
    @JsonProperty("kty")
    val keyType: String,
    @JsonProperty("n")
    val modulus: String,
    @JsonProperty("e")
    val exponent: String,
)

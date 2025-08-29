package com.yapp.brake.oauth.apple.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import java.util.Base64

fun parseHeaders(idToken: String): Map<String, String> {
    if (idToken.isBlank() || idToken.count { it == '.' } < 2) {
        throw CustomException(ErrorCode.TOKEN_INVALID)
    }

    return try {
        val encodedHeader = idToken.substringBefore(".")
        val decodedHeader = String(Base64.getUrlDecoder().decode(encodedHeader))
        jacksonObjectMapper().readValue(decodedHeader, object : TypeReference<Map<String, String>>() {})
    } catch (e: Exception) {
        throw CustomException(ErrorCode.TOKEN_INVALID)
    }
}

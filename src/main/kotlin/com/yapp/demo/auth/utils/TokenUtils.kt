package com.yapp.demo.auth.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import java.util.Base64

fun parseHeaders(idToken: String): Map<String, String> {
    return try {
        val encodedHeader = idToken.substringBefore(".")
        val decodedHeader = String(Base64.getUrlDecoder().decode(encodedHeader))
        jacksonObjectMapper().readValue(decodedHeader, object : TypeReference<Map<String, String>>() {})
    } catch (e: Exception) {
        throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
    }
}

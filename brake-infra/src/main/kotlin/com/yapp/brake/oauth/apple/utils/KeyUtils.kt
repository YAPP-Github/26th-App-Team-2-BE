package com.yapp.brake.oauth.apple.utils

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.util.Base64

fun toRSAPublicKey(
    modulus: String,
    exponent: String,
): PublicKey {
    val decodedModulus = decodeUrl(modulus)
    val decodedExponent = decodeUrl(exponent)

    val keySpec =
        RSAPublicKeySpec(
            BigInteger(1, decodedModulus),
            BigInteger(1, decodedExponent),
        )

    return try {
        KeyFactory.getInstance("RSA").generatePublic(keySpec)
    } catch (e: Exception) {
        throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
    }
}

fun parseECPrivateKey(key: String): PrivateKey {
    val encodedKey = decode(key)
    val keySpec = PKCS8EncodedKeySpec(encodedKey)

    return KeyFactory
        .getInstance("EC")
        .generatePrivate(keySpec)
}

private fun decodeUrl(src: String): ByteArray =
    try {
        Base64.getUrlDecoder().decode(src)
    } catch (e: Exception) {
        throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

private fun decode(src: String): ByteArray =
    try {
        Base64.getDecoder().decode(src)
    } catch (e: Exception) {
        throw CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

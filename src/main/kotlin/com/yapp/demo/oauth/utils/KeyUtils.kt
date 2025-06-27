package com.yapp.demo.oauth.utils

import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
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
    val decodedModulus = Base64.getUrlDecoder().decode(modulus)
    val decodedExponent = Base64.getUrlDecoder().decode(exponent)

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
    val encodedKey = Base64.getDecoder().decode(key)
    val keySpec = PKCS8EncodedKeySpec(encodedKey)

    return KeyFactory
        .getInstance("EC")
        .generatePrivate(keySpec)
}

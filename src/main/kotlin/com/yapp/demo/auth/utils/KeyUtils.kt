package com.yapp.demo.auth.utils

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
    val encodedModulus =
        Base64.getUrlDecoder().decode(modulus)
    val encodedExponent =
        Base64.getUrlDecoder().decode(exponent)

    val spec =
        RSAPublicKeySpec(
            BigInteger(1, encodedModulus),
            BigInteger(1, encodedExponent),
        )

    return KeyFactory
        .getInstance("RSA")
        .generatePublic(spec)
}

fun parseECPrivateKey(keyContent: String): PrivateKey {
    val keyBytes = Base64.getDecoder().decode(keyContent)
    val keySpec = PKCS8EncodedKeySpec(keyBytes)

    return KeyFactory
        .getInstance("EC")
        .generatePrivate(keySpec)
}

package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.utils.parseHeaders
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component

@Component
class AppleIdTokenVerifier(
    private val applePublicKeyProvider: ApplePublicKeyProvider,
    private val appleProperties: AppleProperties,
) {
    fun verify(idToken: String): Claims {
        val header = parseHeaders(idToken)
        val kid = header[appleProperties.headerField] ?: throw CustomException(ErrorCode.UNAUTHORIZED)

        val publicKey = applePublicKeyProvider.getPublicKey(kid)

        return Jwts.parser()
            .verifyWith(publicKey)
            .requireIssuer(appleProperties.audience)
            .build()
            .parseSignedClaims(idToken)
            .payload
    }
}

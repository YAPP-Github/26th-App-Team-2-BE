package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.external.apple.feign.AppleAuthFeignClient
import com.yapp.demo.auth.utils.parseHeaders
import com.yapp.demo.auth.utils.toRSAPublicKey
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.PublicKey

@Component
class AppleIdTokenVerifier(
    private val appleAuthFeignClient: AppleAuthFeignClient,
    private val appleProperties: AppleProperties,
) {
    fun verify(idToken: String): Claims {
        val publicKey = fetchApplePublicKey(idToken)

        return Jwts.parser()
            .verifyWith(publicKey)
            .requireIssuer(appleProperties.audience)
            .build()
            .parseSignedClaims(idToken)
            .payload
    }

    private fun fetchApplePublicKey(idToken: String): PublicKey {
        val header = parseHeaders(idToken)
        val kid = header["kid"] ?: throw CustomException(ErrorCode.UNAUTHORIZED)

        val jwk =
            appleAuthFeignClient.getApplePublicKeys()
                .getMatchesKey(kid)

        return toRSAPublicKey(jwk.modulus, jwk.exponent)
    }
}

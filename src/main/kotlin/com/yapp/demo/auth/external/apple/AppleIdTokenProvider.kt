package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.external.apple.feign.AppleAuthFeignClient
import com.yapp.demo.auth.internal.AppleProperties
import com.yapp.demo.auth.utils.parseHeaders
import com.yapp.demo.auth.utils.toRSAPublicKey
import com.yapp.demo.common.exception.CustomException
import com.yapp.demo.common.exception.ErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.PublicKey

private val logger = KotlinLogging.logger {}

@Component
class AppleIdTokenProvider(
    private val appleAuthFeignClient: AppleAuthFeignClient,
    private val appleProperties: AppleProperties,
) {
    fun getIdToken(
        code: String,
        clientSecret: String,
    ): String {
        return try {
            appleAuthFeignClient.generateTokens(
                clientId = appleProperties.clientId,
                clientSecret = clientSecret,
                code = code,
                grantType = APPLE_AUTH_GRANT_TYPE,
            ).idToken
        } catch (e: Exception) {
            logger.error(e) { "[AppleAuthProvider.getIdToken] code=$code" }
            throw e
        }
    }

    fun verifyAndParse(idToken: String): Claims {
        val header = parseHeaders(idToken)
        val kid = header[appleProperties.headerField] ?: throw CustomException(ErrorCode.UNAUTHORIZED)

        val publicKey = getPublicKey(kid)

        return Jwts.parser()
            .verifyWith(publicKey)
            .requireIssuer(appleProperties.audience)
            .build()
            .parseSignedClaims(idToken)
            .payload
    }

    private fun getPublicKey(kid: String): PublicKey {
        val applePublicKeys = appleAuthFeignClient.getApplePublicKeys()
        val matched = applePublicKeys.getMatchedKey(kid)

        return toRSAPublicKey(matched.modulus, matched.exponent)
    }

    companion object {
        private const val APPLE_AUTH_GRANT_TYPE = "authorization_code"
    }
}

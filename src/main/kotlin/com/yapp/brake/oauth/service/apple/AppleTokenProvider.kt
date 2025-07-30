package com.yapp.brake.oauth.service.apple

import com.yapp.brake.common.exception.CustomException
import com.yapp.brake.common.exception.ErrorCode
import com.yapp.brake.oauth.infrastructure.feign.apple.AppleAuthFeignClient
import com.yapp.brake.oauth.infrastructure.feign.apple.response.AppleTokenResponse
import com.yapp.brake.oauth.utils.parseHeaders
import com.yapp.brake.oauth.utils.toRSAPublicKey
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.PublicKey

private val logger = KotlinLogging.logger {}

@Component
class AppleTokenProvider(
    private val appleAuthFeignClient: AppleAuthFeignClient,
    private val appleProperties: AppleProperties,
) {
    fun getToken(
        code: String,
        clientSecret: String,
    ): AppleTokenResponse {
        return try {
            appleAuthFeignClient.generateTokens(
                clientId = appleProperties.clientId,
                clientSecret = clientSecret,
                code = code,
                grantType = APPLE_AUTH_GRANT_TYPE,
            )
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

    fun revokeToken(
        token: String,
        clientSecret: String,
    ) {
        appleAuthFeignClient.revokeToken(
            clientId = appleProperties.clientId,
            clientSecret = clientSecret,
            token = token,
            tokenTypeHint = TOKEN_TYPE,
        )
    }

    companion object {
        private const val APPLE_AUTH_GRANT_TYPE = "authorization_code"
        private const val TOKEN_TYPE = "refresh_token"
    }
}

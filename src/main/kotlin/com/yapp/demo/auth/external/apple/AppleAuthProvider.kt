package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.auth.external.OAuthUserInfo
import com.yapp.demo.auth.external.apple.feign.AppleAuthFeignClient
import com.yapp.demo.common.enums.SocialProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.PrivateKey
import java.time.ZonedDateTime
import java.util.Date

private val logger = KotlinLogging.logger {}

@Component
class AppleAuthProvider(
    private val appleProperties: AppleProperties,
    private val appleAuthFeignClient: AppleAuthFeignClient,
    private val applePrivateKey: PrivateKey,
    private val jwtVerifier: AppleIdTokenVerifier,
) : OAuthProvider {
    /**
     * validate code and generate tokens
     */
    override fun getAccessToken(code: String): String {
        return try {
            appleAuthFeignClient.generateTokens(
                clientId = appleProperties.clientId,
                clientSecret = createClientSecret(),
                code = code,
                grantType = APPLE_AUTH_GRANT_TYPE,
            ).idToken
        } catch (e: Exception) {
            logger.error(e) { "[AppleAuthProvider.getIdToken] code=$code" }
            ""
        }
    }

    fun createClientSecret(): String {
        val expiration = ZonedDateTime.now().plusMinutes(5)

        return Jwts.builder()
            .header()
            .add("kid", appleProperties.keyId)
            .and()
            .issuer(appleProperties.teamId)
            .audience().add(appleProperties.audience).and()
            .subject(appleProperties.clientId)
            .expiration(Date.from(expiration.toInstant()))
            .issuedAt(Date())
            .signWith(applePrivateKey, Jwts.SIG.ES256)
            .compact()
    }

    /**
     * parse IdToken
     */
    override fun getUserInfo(token: String): OAuthUserInfo {
        val claims = jwtVerifier.verify(token)
        val userId = claims.subject
        val email = claims["email"] as? String ?: "empty"
        val name = claims["name"] as? String

        return OAuthUserInfo(
            id = userId,
            email = email,
        )
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.APPLE

    companion object {
        private const val APPLE_AUTH_GRANT_TYPE = "authorization_code"
    }
}

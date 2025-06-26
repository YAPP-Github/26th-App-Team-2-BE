package com.yapp.demo.auth.external.apple

import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.auth.external.OAuthUserInfo
import com.yapp.demo.auth.external.apple.feign.AppleAuthFeignClient
import com.yapp.demo.common.enums.SocialProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class AppleAuthProvider(
    private val appleAuthFeignClient: AppleAuthFeignClient,
    private val appleProperties: AppleProperties,
    private val appleClientSecretProvider: AppleClientSecretProvider,
    private val idTokenVerifier: AppleIdTokenVerifier,
) : OAuthProvider {
    /**
     * validate code and generate tokens
     */
    override fun getAccessToken(code: String): String {
        return try {
            appleAuthFeignClient.generateTokens(
                clientId = appleProperties.clientId,
                clientSecret = appleClientSecretProvider.getClientSecret(),
                code = code,
                grantType = APPLE_AUTH_GRANT_TYPE,
            ).idToken
        } catch (e: Exception) {
            logger.error(e) { "[AppleAuthProvider.getIdToken] code=$code" }
            throw e
        }
    }

    /**
     * parse IdToken
     */
    override fun getUserInfo(token: String): OAuthUserInfo {
        val claims = idTokenVerifier.verify(token)
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

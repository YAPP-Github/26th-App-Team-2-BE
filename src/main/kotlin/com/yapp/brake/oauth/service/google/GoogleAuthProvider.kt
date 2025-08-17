package com.yapp.brake.oauth.service.google

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.oauth.infrastructure.feign.google.GoogleApiFeignClient
import com.yapp.brake.oauth.infrastructure.feign.google.GoogleAuthFeignClient
import com.yapp.brake.oauth.infrastructure.feign.google.response.GoogleTokenResponse
import com.yapp.brake.oauth.infrastructure.feign.google.response.GoogleUserInfoResponse
import com.yapp.brake.oauth.model.OAuthUserInfo
import com.yapp.brake.oauth.service.OAuthProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class GoogleAuthProvider(
    private val googleProperties: GoogleProperties,
    private val googleAuthFeignClient: GoogleAuthFeignClient,
    private val googleApiFeignClient: GoogleApiFeignClient,
) : OAuthProvider {
    override fun getOAuthUserInfo(code: String): OAuthUserInfo {
        val response =
            try {
                googleAuthFeignClient.getToken(
                    grantType = GOOGLE_AUTH_GRANT_TYPE,
                    clientId = googleProperties.clientId,
                    clientSecret = googleProperties.clientSecret,
                    redirectUri = googleProperties.redirectUri,
                    code = code,
                )
            } catch (e: Exception) {
                logger.error(e) { "[GoogleAuthProvider.getToken] code=$code" }
                GoogleTokenResponse.createEmpty()
            }

        return getUserInfo(response.accessToken, response.refreshToken)
    }

    override fun withdraw(credential: String) {
        try {
            googleAuthFeignClient.revokeToken(credential)
        } catch (e: Exception) {
            logger.error(e) { "[GoogleAuthProvider.withdraw] code=$credential" }
        }
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.GOOGLE

    private fun getUserInfo(
        accessToken: String,
        refreshToken: String,
    ): OAuthUserInfo {
        val userInfo =
            try {
                googleApiFeignClient.getUserInfo("$GOOGLE_AUTH_HEADER_PREFIX$accessToken")
            } catch (e: Exception) {
                logger.error(e) { "[GoogleAuthProvider.getUserInfo] token=$accessToken" }
                GoogleUserInfoResponse.createEmpty()
                throw e
            }

        return OAuthUserInfo(
            socialProvider = SocialProvider.KAKAO,
            credential = refreshToken,
            email = userInfo.email,
        )
    }

    companion object {
        private const val GOOGLE_AUTH_GRANT_TYPE = "authorization_code"
        private const val GOOGLE_AUTH_HEADER_PREFIX = "Bearer "
    }
}

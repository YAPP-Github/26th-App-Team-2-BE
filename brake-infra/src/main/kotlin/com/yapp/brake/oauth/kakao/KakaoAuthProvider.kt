package com.yapp.brake.oauth.service.kakao

import com.yapp.brake.common.enums.SocialProvider
import com.yapp.brake.oauth.kakao.client.KakaoApiFeignClient
import com.yapp.brake.oauth.kakao.client.KakaoAuthFeignClient
import com.yapp.brake.oauth.kakao.client.response.KakaoUserInfoResponse
import com.yapp.brake.oauth.model.OAuthUserInfo
import com.yapp.brake.oauth.service.OAuthProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class KakaoAuthProvider(
    private val kakaoProperties: KakaoProperties,
    private val kakaoAuthFeignClient: KakaoAuthFeignClient,
    private val kakaoApiFeignClient: KakaoApiFeignClient,
) : OAuthProvider {
    override fun getOAuthUserInfo(code: String): OAuthUserInfo {
        val accessToken =
            try {
                kakaoAuthFeignClient.getToken(
                    grantType = KAKAO_AUTH_GRANT_TYPE,
                    clientId = kakaoProperties.clientId,
                    clientSecret = kakaoProperties.clientSecret,
                    redirectUri = kakaoProperties.redirectUri,
                    code = code,
                ).accessToken
            } catch (e: Exception) {
                logger.error(e) { "[KakaoAuthProvider.getAccessToken] code=$code" }
                ""
            }

        return getUserInfo(accessToken)
    }

    private fun getUserInfo(token: String): OAuthUserInfo {
        val userInfo =
            try {
                kakaoApiFeignClient.getUserInfo("$KAKAO_AUTH_HEADER_PREFIX$token")
            } catch (e: Exception) {
                logger.error(e) { "[KakaoAuthProvider.getUserInfo] token=$token" }
                KakaoUserInfoResponse.createEmpty()
                throw e
            }

        return OAuthUserInfo(
            socialProvider = SocialProvider.KAKAO,
            credential = userInfo.id,
            email = userInfo.kakaoAccount.email,
        )
    }

    override fun withdraw(credential: String) {
        try {
            kakaoApiFeignClient.unlink(
                adminKey = "$KAKAO_UNLINK_HEADER_PREFIX${kakaoProperties.adminKey}",
                targetIdType = KAKAO_UNLINK_TARGET_ID_TYPE,
                targetId = credential.toLong(),
            )
        } catch (e: Exception) {
            logger.error(e) { "[KakaoAuthProvider.withdraw] code=$credential" }
        }
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.KAKAO

    companion object {
        private const val KAKAO_AUTH_GRANT_TYPE = "authorization_code"
        private const val KAKAO_AUTH_HEADER_PREFIX = "Bearer "
        private const val KAKAO_UNLINK_HEADER_PREFIX = "KakaoAK "
        private const val KAKAO_UNLINK_TARGET_ID_TYPE = "user_id"
    }
}

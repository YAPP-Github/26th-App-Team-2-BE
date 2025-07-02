package com.yapp.demo.oauth.service.kakao

import com.yapp.demo.common.enums.SocialProvider
import com.yapp.demo.oauth.infrastructure.feign.kakao.KakaoApiFeignClient
import com.yapp.demo.oauth.infrastructure.feign.kakao.KakaoAuthFeignClient
import com.yapp.demo.oauth.infrastructure.feign.kakao.response.KakaoUserInfoResponse
import com.yapp.demo.oauth.model.OAuthUserInfo
import com.yapp.demo.oauth.service.OAuthProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class KakaoAuthProvider(
    private val kakaoProperties: KakaoProperties,
    private val kakaoAuthFeignClient: KakaoAuthFeignClient,
    private val kakaoApiFeignClient: KakaoApiFeignClient,
) : OAuthProvider {
    override fun getAccessToken(code: String): String {
        return try {
            kakaoAuthFeignClient.getToken(
                grantType = KAKAO_AUTH_GRANT_TYPE,
                clientId = kakaoProperties.clientId,
                redirectUri = kakaoProperties.redirectUri,
                code = code,
            ).accessToken
        } catch (e: Exception) {
            logger.error(e) { "[KakaoAuthProvider.getAccessToken] code=$code" }
            ""
        }
    }

    override fun getUserInfo(token: String): OAuthUserInfo {
        val userInfo =
            try {
                kakaoApiFeignClient.getUserInfo("$KAKAO_AUTH_HEADER_PREFIX$token")
            } catch (e: Exception) {
                logger.error(e) { "[KakaoAuthProvider.getUserInfo] token=$token" }
                KakaoUserInfoResponse.createEmpty()
                throw e
            }

        return OAuthUserInfo(
            id = userInfo.id,
            email = userInfo.kakaoAccount.email,
        )
    }

    override fun withdraw(code: String) {
        try {
            val accessToken =
                kakaoAuthFeignClient.getToken(
                    grantType = KAKAO_AUTH_GRANT_TYPE,
                    clientId = kakaoProperties.clientId,
                    redirectUri = kakaoProperties.redirectUri,
                    code = code,
                ).accessToken

            kakaoApiFeignClient.unlink("$KAKAO_AUTH_HEADER_PREFIX$accessToken")
        } catch (e: Exception) {
            logger.error(e) { "[KakaoAuthProvider.withdraw] code=$code" }
        }
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.KAKAO

    companion object {
        private const val KAKAO_AUTH_GRANT_TYPE = "authorization_code"
        private const val KAKAO_AUTH_HEADER_PREFIX = "Bearer "
    }
}

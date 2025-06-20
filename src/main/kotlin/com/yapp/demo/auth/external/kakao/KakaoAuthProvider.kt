package com.yapp.demo.auth.external.kakao

import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.auth.external.OAuthUserInfo
import com.yapp.demo.auth.external.kakao.feign.KakaoAuthFeignClient
import com.yapp.demo.auth.external.kakao.feign.KakaoUserInfoFeignClient
import com.yapp.demo.auth.external.kakao.feign.response.KakaoUserInfoResponse
import com.yapp.demo.common.enums.SocialProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class KakaoAuthProvider(
    @Value("\${oauth.kakao.client-id}")
    private val clientId: String,
    @Value("\${oauth.kakao.redirect-uri}")
    private val redirectUri: String,
    private val kakaoAuthFeignClient: KakaoAuthFeignClient,
    private val kakaoUserInfoFeignClient: KakaoUserInfoFeignClient,
) : OAuthProvider {
    override fun getAccessToken(code: String): String {
        return try {
            kakaoAuthFeignClient.getToken(
                grantType = KAKAO_AUTH_GRANT_TYPE,
                clientId = clientId,
                redirectUri = redirectUri,
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
                kakaoUserInfoFeignClient.getUserInfo("$KAKAO_AUTH_HEADER_PREFIX$token")
            } catch (e: Exception) {
                logger.error(e) { "[KakaoAuthProvider.getUserInfo] token=$token" }
                KakaoUserInfoResponse.createEmpty()
            }

        return OAuthUserInfo(
            id = userInfo.id,
            email = userInfo.kakaoAccount.email,
        )
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.KAKAO

    companion object {
        private const val KAKAO_AUTH_GRANT_TYPE = "authorization_code"
        private const val KAKAO_AUTH_HEADER_PREFIX = "Bearer "
    }
}

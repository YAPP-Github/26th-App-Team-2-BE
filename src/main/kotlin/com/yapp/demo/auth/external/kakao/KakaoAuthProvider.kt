package com.yapp.demo.auth.external.kakao

import com.yapp.demo.auth.external.OAuthProvider
import com.yapp.demo.auth.external.OAuthUserInfo
import com.yapp.demo.auth.external.kakao.feign.KakaoAuthFeignClient
import com.yapp.demo.auth.external.kakao.feign.KakaoUserInfoFeignClient
import com.yapp.demo.common.enums.SocialProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KakaoAuthProvider(
    @Value("\${oauth.kakao.client-id}")
    private val clientId: String,
    @Value("\${oauth.kakao.redirect-uri}")
    private val redirectUri: String,
    private val kakaoAuthFeignClient: KakaoAuthFeignClient,
    private val kakaoUserInfoFeignClient: KakaoUserInfoFeignClient,
) : OAuthProvider {
    override fun getAccessToken(code: String): String =
        kakaoAuthFeignClient.getToken(
            grantType = KAKAO_AUTH_GRANT_TYPE,
            clientId = clientId,
            redirectUri = redirectUri,
            code = code,
        ).accessToken

    override fun getUserInfo(token: String): OAuthUserInfo {
        val userInfo = kakaoUserInfoFeignClient.getUserInfo(token)
        return OAuthUserInfo(
            id = userInfo.id,
            email = userInfo.kakaoAccount.email,
        )
    }

    override fun supports(socialType: SocialProvider): Boolean = socialType == SocialProvider.KAKAO

    companion object {
        private const val KAKAO_AUTH_GRANT_TYPE = "authorization_code"
    }
}

package com.yapp.brake.oauth.kakao.client

import com.yapp.brake.oauth.kakao.client.response.KakaoTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "kakao-auth-client",
    url = "https://kauth.kakao.com",
    configuration = [com.yapp.brake.oauth.kakao.client.KakaoFeignConfig::class],
)
interface KakaoAuthFeignClient {
    @PostMapping("/oauth/token")
    fun getToken(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("code") code: String,
    ): com.yapp.brake.oauth.kakao.client.response.KakaoTokenResponse
}

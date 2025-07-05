package com.yapp.brake.oauth.infrastructure.feign.kakao

import com.yapp.brake.oauth.infrastructure.feign.kakao.response.KakaoTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "kakao-auth-client",
    url = "https://kauth.kakao.com",
    configuration = [KakaoFeignConfig::class],
)
interface KakaoAuthFeignClient {
    @PostMapping("/oauth/token")
    fun getToken(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("code") code: String,
    ): KakaoTokenResponse
}

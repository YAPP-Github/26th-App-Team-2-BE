package com.yapp.demo.oauth.infrastructure.feign.apple

import com.yapp.demo.oauth.infrastructure.feign.apple.response.ApplePublicKeyResponse
import com.yapp.demo.oauth.infrastructure.feign.apple.response.AppleTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "apple-auth-client",
    url = "https://appleid.apple.com",
    configuration = [AppleFeignConfig::class],
)
interface AppleAuthFeignClient {
    @PostMapping(
        "/auth/oauth2/v2/token",
        headers = ["Content-Type=application/x-www-form-urlencoded"],
    )
    fun generateTokens(
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("code") code: String,
        @RequestParam("grant_type") grantType: String,
    ): AppleTokenResponse

    @GetMapping("/auth/oauth2/v2/keys")
    fun getApplePublicKeys(): ApplePublicKeyResponse
}

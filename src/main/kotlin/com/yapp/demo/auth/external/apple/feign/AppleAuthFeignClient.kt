package com.yapp.demo.auth.external.apple.feign

import com.yapp.demo.auth.external.apple.feign.response.ApplePublicKeyResponse
import com.yapp.demo.auth.external.apple.feign.response.AppleTokenResponse
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
        "/auth/token",
        headers = ["Content-Type=application/x-www-form-urlencoded"],
    )
    fun generateTokens(
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("code") code: String,
        @RequestParam("grant_type") grantType: String,
    ): AppleTokenResponse

    @GetMapping("/auth/keys")
    fun getApplePublicKeys(): ApplePublicKeyResponse
}

package com.yapp.brake.oauth.infrastructure.feign.google

import com.yapp.brake.oauth.infrastructure.feign.google.response.GoogleTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "google-auth-client",
    url = "https://oauth2.googleapis.com/token",
    configuration = [GoogleFeignConfig::class],
)
interface GoogleAuthFeignClient {
    @PostMapping
    fun getToken(
        @RequestParam(name = "code") code: String,
        @RequestParam(name = "client_id") clientId: String,
        @RequestParam(name = "client_secret") clientSecret: String,
        @RequestParam(name = "redirect_uri") redirectUri: String,
        @RequestParam(name = "grant_type") grantType: String,
    ): GoogleTokenResponse

    @PostMapping("/revoke")
    fun revokeToken(
        @RequestParam("token") token: String,
    )
}
